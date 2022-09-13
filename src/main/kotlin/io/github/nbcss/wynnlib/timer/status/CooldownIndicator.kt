package io.github.nbcss.wynnlib.timer.status

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.roundToInt

class CooldownIndicator(data: JsonObject): StatusType(data) {
    companion object: Factory {
        override fun create(data: JsonObject): StatusType {
            return CooldownIndicator(data)
        }
        override fun getKey(): String = "COOLDOWN"
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
        RenderKit.renderTexture(
            matrices, ICON_BACKGROUND, posX + 3, posY, 0, 256 - 22, 22, 22
        )
        val duration: Double? = timer.getDuration()
        val maxDuration: Double? = timer.getFullDuration()
        if(duration != null && maxDuration != null) {
            val pct = MathHelper.clamp(duration / maxDuration, 0.0, 1.0)
            val color = Color.AQUA
            val uv = pctToUv(1 - pct)
            RenderKit.renderTextureWithColor(
                matrices, ICON_BACKGROUND, color.solid(),
                posX + 3, posY, uv.first, uv.second, 22, 22, 256, 256
            )
            var time = duration.roundToInt().toString() + "s"
            if (duration < 9.95) {
                time = String.format("%.1fs", duration)
            }
            val textX: Int = posX + 14 - textRenderer.getWidth(time) / 2
            val textY = posY + 25
            val text: Text = LiteralText(time).formatted(Formatting.AQUA)
            RenderKit.renderOutlineText(matrices, text, textX.toFloat(), textY.toFloat())
        }
        RenderKit.renderTexture(
            matrices, icon, posX + 5, posY + 2, 0, 0, 18, 18, 18, 18
        )
    }
}