package io.github.nbcss.wynnlib.timer.indicators

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.parseStyle
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class ValuesIndicator(data: JsonObject): StatusType(data) {
    companion object: Factory {
        override fun create(data: JsonObject): StatusType {
            return ValuesIndicator(data)
        }
        override fun getKey(): String = "VALUE"
    }
    private val cap: Int?
    private val format: String
    init {
        val properties = data["properties"].asJsonObject
        cap = if (properties.has("max")) properties["max"].asInt else null
        format = if (properties.has("format")) properties["format"].asString else "%s"
    }

    private fun formatValue(value: Int): String {
        return if (value >= 10000) {
            "${value / 1000}K"
        }else if (value >= 1000) {
            String.format("%.1fK", value / 1000.0)
        }else{
            "$value"
        }
    }

    override fun renderIcon(matrices: MatrixStack,
                            textRenderer: TextRenderer,
                            timer: TypedStatusTimer,
                            icon: Identifier,
                            posX: Int,
                            posY: Int) {
        RenderKit.renderTexture(
            matrices, ICON_BACKGROUND, posX + 3, posY, 0, 256 - 22, 22, 22
        )
        //render value text
        val values = timer.getValues()
        if (values.isNotEmpty()) {
            val rate: Double = values[0].toDouble() / (cap ?: timer.getMaxValues()[0])
            val pct = MathHelper.clamp(rate, 0.0, 1.0)
            val color = Color(MathHelper.hsvToRgb((pct / 3.0).toFloat(), 1.0f, 1.0f))
            val uv = pctToUv(pct)
            RenderKit.renderTextureWithColor(
                matrices, ICON_BACKGROUND, color.toSolidColor(),
                posX + 3, posY, uv.first, uv.second, 22, 22, 256, 256
            )
            val formattedValue = formatValue(values[0])
            val valueText = try {
                parseStyle(format.format(formattedValue), Formatting.GRAY.toString())
            }catch (e: Exception){
                formattedValue
            }
            val textX: Int = posX + 14 - textRenderer.getWidth(valueText) / 2
            val textY = posY + 25
            RenderKit.renderOutlineText(matrices, LiteralText(valueText), textX.toFloat(), textY.toFloat())
        }
        //render icon
        RenderKit.renderTexture(
            matrices, icon, posX + 5, posY + 2, 0, 0, 18, 18, 18, 18
        )
    }
}