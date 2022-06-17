package io.github.nbcss.wynnlib.gui

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

interface TooltipScreen {
    fun drawTooltip(matrices: MatrixStack, tooltip: List<Text>, x: Int, y: Int)
}