package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack

interface IconIndicator: Keyed {
    fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int, delta: Float)
}