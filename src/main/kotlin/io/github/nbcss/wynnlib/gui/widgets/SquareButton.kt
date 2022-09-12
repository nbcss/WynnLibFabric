package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier

class SquareButton(private val texture: Identifier,
                   x: Int,
                   y: Int,
                   private val size: Int,
                   onPress: PressAction):
    ButtonWidget(x, y, size, size, LiteralText.EMPTY, onPress) {
    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val v = if (this.isHovered) size else 0
        RenderKit.renderTexture(matrices, texture, x, y, 0, v, size, size, size, size * 2)
        if (this.isHovered) {
            //todo tooltip
        }
    }
}