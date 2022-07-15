package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.gui.ability.AbstractAbilityTreeScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import kotlin.math.max

abstract class ScrollViewerWidget(private val screen: TooltipScreen,
                         private val x: Int,
                         private val y: Int,
                         private val width: Int,
                         private val height: Int,
                         private val scrollUnit: Int = 32,
                         private val scrollTicks: Int = 5):
    DrawableHelper(), Drawable, Element{
    protected val client: MinecraftClient = MinecraftClient.getInstance()
    protected var scrollPos: Int = 0
    protected var renderPos: Int = 0
    protected var ticker: Int = -1

    abstract fun renderViewer(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float)

    abstract fun getMaxScrollPosition(): Int

    fun isOverViewer(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }

    fun setScrollPosition(position: Int) {
        scrollPos = MathHelper.clamp(position, 0, getMaxScrollPosition())
        ticker = scrollTicks
    }

    fun tick(){
        ticker = max(-1, ticker - 1)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if (ticker >= 0){
            renderPos += ((scrollPos - renderPos).toFloat() / ticker.toFloat()).toInt()
        }else {
            renderPos = scrollPos
        }
        val bottom = y + height
        val scale = client.window.scaleFactor
        RenderSystem.enableScissor((x * scale).toInt(), client.window.height - (bottom * scale).toInt(),
            (width * scale).toInt(), (height * scale).toInt())
        //todo render background
        renderViewer(matrices!!, mouseX, mouseY, delta)
        RenderSystem.disableScissor()
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (isOverViewer(mouseX.toInt(), mouseY.toInt())){
            setScrollPosition(scrollPos - amount.toInt() * scrollUnit)
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }
}