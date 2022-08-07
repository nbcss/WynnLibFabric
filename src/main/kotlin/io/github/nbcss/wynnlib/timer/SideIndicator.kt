package io.github.nbcss.wynnlib.timer

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack

interface SideIndicator {
    fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int)
}