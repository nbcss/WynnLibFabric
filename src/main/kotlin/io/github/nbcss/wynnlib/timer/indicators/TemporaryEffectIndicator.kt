package io.github.nbcss.wynnlib.timer.indicators

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class TemporaryEffectIndicator(data: JsonObject): StatusType(data) {
    companion object: Factory {
        override fun create(data: JsonObject): StatusType {
            return TemporaryEffectIndicator(data)
        }
        override fun getKey(): String = "TEMP_EFFECT"
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
        RenderKit.renderTexture(
            matrices, icon, posX + 5, posY + 2, 0, 0, 18, 18, 18, 18
        )
    }
}