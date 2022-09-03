package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import kotlin.math.min
import kotlin.math.roundToInt

class RollingTextWidget(val x: Int,
                        val y: Int,
                        val width: Int,
                        private var text: Text? = null): DrawableHelper() {
    companion object {
        private val client: MinecraftClient = MinecraftClient.getInstance()
        private const val stayTime = 1200
        private const val rollingSpeed = 0.025f
    }

    fun setText(text: Text?) {
        this.text = text
    }

    fun render(matrices: MatrixStack) {
        text?.let {
            val length = client.textRenderer.getWidth(text)
            val color = Color.WHITE.toSolidColor().getColorCode()
            val bottom = y + client.textRenderer.fontHeight
            val scale = client.window.scaleFactor
            var textX = x.toFloat()
            if (length > width) {
                val time = System.currentTimeMillis()
                val rollingDuration = ((length - width) / rollingSpeed).roundToInt()
                val duration = stayTime * 2 + rollingDuration * 2
                val index = time % duration
                if (index > stayTime * 2 + rollingDuration) {
                    textX -= (length - width) - (index - stayTime * 2 - rollingDuration) * rollingSpeed
                }else if(index > stayTime) {
                    textX -= min((index - stayTime) * rollingSpeed, (length - width).toFloat())
                }
            }
            RenderSystem.enableScissor((x * scale).toInt(), client.window.height - (bottom * scale).toInt(),
                (width * scale).toInt(), (client.textRenderer.fontHeight * scale).toInt())
            client.textRenderer.draw(matrices, it, textX, y.toFloat(), color)
            RenderSystem.disableScissor()
        }
    }
}