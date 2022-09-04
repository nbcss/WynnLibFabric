package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import io.github.nbcss.wynnlib.timer.status.ValuesIndicator
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import kotlin.math.*

abstract class ScrollPaneWidget(private val background: TextureData,
                                private val screen: TooltipScreen,
                                val x: Int,
                                val y: Int,
                                val width: Int,
                                val height: Int,
                                val scrollDelay: Long = 200L,
                                private val scrollUnit: Double = 32.0):
    DrawableHelper(), Drawable, Element{
    protected val client: MinecraftClient = MinecraftClient.getInstance()
    private var position: Double = 0.0
    private var dragging: Pair<Double, Double>? = null
    private var scrolling: ScrollChange? = null

    abstract fun renderContents(matrices: MatrixStack, mouseX: Int, mouseY: Int, position: Double, delta: Float)

    abstract fun getContentHeight(): Int

    open fun renderContentsPost(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float){

    }

    open fun onContentClick(mouseX: Double, mouseY: Double, button: Int): Boolean = false

    open fun onContentRelease(mouseX: Double, mouseY: Double): Boolean = false

    open fun getSlider(): VerticalSliderWidget? = null

    open fun renderBackground(matrices: MatrixStack?, position: Double) {
        val maxPos = getMaxPosition()
        val factor = if (maxPos == 0.0) 0.0 else MathHelper.clamp(position / maxPos, 0.0, 1.0)
        val offset = factor * max(0, (background.height - background.v) - height)
        RenderKit.renderTexture(matrices!!,
            background.texture,
            x.toDouble(),
            y - offset,
            background.u,
            background.v,
            width,
            background.height - background.v,
            background.width,
            background.height)
    }

    private fun updateSlider() {
        getSlider()?.setSlider(position / getMaxPosition())
    }

    fun getMaxPosition(): Double {
        return max(0, getContentHeight() - height).toDouble()
    }

    fun getScrollPosition(): Double {
        return position
    }

    fun setScrollPosition(position: Double, delay: Long = 0L) {
        val pos = MathHelper.clamp(position, 0.0, getMaxPosition())
        if (delay <= 0) {
            this.position = pos
        }else{
            val time = System.currentTimeMillis()
            this.scrolling = ScrollChange(this.position, pos, delay, time, time)
        }
    }

    fun reset() {
        this.scrolling = null
        this.position = 0.0
        updateSlider()
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        scrolling = scrolling?.update()
        scrolling?.let {
            position = MathHelper.clamp(it.getCurrentPosition(), 0.0, getMaxPosition())
            updateSlider()
        }
        val bottom = y + height
        val scale = client.window.scaleFactor
        val position = getScrollPosition()
        RenderSystem.enableScissor((x * scale).toInt(), client.window.height - (bottom * scale).toInt(),
            (width * scale).toInt(), (height * scale).toInt())
        renderBackground(matrices, position)
        renderContents(matrices!!, mouseX, mouseY, position, delta)
        RenderSystem.disableScissor()
        matrices.push()
        matrices.translate(0.0, 0.0, 200.0)
        getSlider()?.render(matrices, mouseX, mouseY, delta)
        matrices.pop()
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (isMouseOver(mouseX, mouseY) && dragging == null){
            val pos = scrolling?.to ?: position
            setScrollPosition(pos - amount.toInt() * scrollUnit, scrollDelay)
            updateSlider()
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            if (onContentClick(mouseX, mouseY, button))
                return true
            if (button == 0 && scrolling == null) {
                dragging = position to mouseY
                return true
            }
        }
        if (getSlider()?.mouseClicked(mouseX, mouseY, button) == true)
            return true
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        dragging?.let {
            setScrollPosition(it.first + (it.second - mouseY))
            updateSlider()
        }
        if (getSlider()?.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) == true)
            return true
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            if (onContentRelease(mouseX, mouseY))
                return true
        }
        if (button == 0 && dragging != null) {
            dragging = null
            return true
        }
        if (getSlider()?.mouseReleased(mouseX, mouseY, button) == true)
            return true
        return super.mouseReleased(mouseX, mouseY, button)
    }

    data class ScrollChange(val from: Double,
                            val to: Double,
                            val duration: Long,
                            val startTime: Long,
                            val currentTime: Long){
        fun update(): ScrollChange? {
            if (currentTime > startTime + duration)
                return null
            return ScrollChange(from, to, duration, startTime, System.currentTimeMillis())
        }

        fun getCurrentPosition(): Double {
            val time = MathHelper.clamp((currentTime - startTime) / duration.toDouble(), 0.0, 1.0)
            val smoother = sin(time * PI / 2)
            return from + smoother * (to - from)
        }
    }
}