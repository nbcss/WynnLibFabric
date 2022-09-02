package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import java.util.function.Consumer
import kotlin.math.roundToInt

class VerticalSliderWidget(x: Int,
                           y: Int,
                           width: Int,
                           height: Int,
                           private val barHeight: Int,
                           private val texture: TextureData,
                           private val onUpdate: Consumer<Double>? = null):
    ClickableWidget(x, y, width, height, null) {
    private var slider: Double = 0.0
    //last click data when start dragging: (slider Y, mouse Y)
    private var dragging: Pair<Int, Double>? = null

    fun getSlider(): Double = slider

    fun setSlider(value: Double) {
        slider = MathHelper.clamp(value, 0.0, 1.0)
    }

    fun isDragging(): Boolean = dragging != null

    private fun getSliderY(): Int {
        return y + (slider * (height - barHeight)).roundToInt()
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if (this.visible) {
            RenderKit.renderTexture(matrices,
                texture.texture,
                x, getSliderY(),
                texture.u,
                texture.v,
                width,
                barHeight,
                texture.width,
                texture.height
            )
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        val dragging = dragging
        if (this.active && this.visible && button == 0 && dragging != null) {
            val sliderY = dragging.first + mouseY - dragging.second
            setSlider((sliderY - y) / (height - barHeight))
            onUpdate?.accept(slider)
            return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0 && dragging != null) {
            dragging = null
            return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (this.active && this.visible && button == 0 && dragging == null && mouseX >= x && mouseX <= x + width){
            val sliderY = getSliderY()
            if (mouseY >= sliderY && mouseY <= sliderY + barHeight) {
                //keep current slider value and mouse y of click point
                dragging = sliderY to mouseY
                return true
            }else if (mouseY >= y && mouseY <= y + height) {
                setSlider((mouseY - barHeight / 2 - y) / (height - barHeight))
                onUpdate?.accept(slider)
                dragging = getSliderY() to mouseY
                return true
            }
        }
        return false
    }

    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }
}