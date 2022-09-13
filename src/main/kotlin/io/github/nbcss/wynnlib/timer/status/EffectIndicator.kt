package io.github.nbcss.wynnlib.timer.status

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.render.RenderKit.renderOutlineText
import io.github.nbcss.wynnlib.render.RenderKit.renderTexture
import io.github.nbcss.wynnlib.render.RenderKit.renderTextureWithColor
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.roundToInt

class EffectIndicator(data: JsonObject): StatusType(data) {
    companion object: Factory {
        override fun create(data: JsonObject): StatusType {
            return EffectIndicator(data)
        }
        override fun getKey(): String = "EFFECT"
    }

    override fun renderIcon(
        matrices: MatrixStack,
        textRenderer: TextRenderer,
        timer: TypedStatusTimer,
        icon: Identifier,
        posX: Int,
        posY: Int,
        delta: Float
    ) {
        renderTexture(
            matrices, ICON_BACKGROUND, posX + 3, posY, 0, 256 - 22, 22, 22
        )
        val duration: Double? = timer.getDuration()
        val maxDuration: Double? = timer.getFullDuration()
        if(duration != null && maxDuration != null) {
            val pct = MathHelper.clamp(duration / maxDuration, 0.0, 1.0)
            val color = Color(MathHelper.hsvToRgb((pct / 3.0).toFloat(), 1.0f, 1.0f))
            val uv = pctToUv(pct)
            renderTextureWithColor(matrices, ICON_BACKGROUND, color.solid(),
                posX + 3, posY, uv.first, uv.second, 22, 22, 256, 256
            )
            var time = duration.roundToInt().toString() + "s"
            if (duration < 9.95) {
                time = String.format("%.1fs", duration)
            }
            val textX: Int = posX + 14 - textRenderer.getWidth(time) / 2
            val textY = posY + 25
            val text: Text = LiteralText(time).formatted(Formatting.LIGHT_PURPLE)
            renderOutlineText(matrices, text, textX.toFloat(), textY.toFloat())
        }
        renderTexture(
            matrices, icon, posX + 5, posY + 2, 0, 0, 18, 18, 18, 18
        )
    }
}