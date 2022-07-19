package io.github.nbcss.wynnlib.items

import net.minecraft.text.Text

interface TooltipProvider {
    fun getTooltip(): List<Text>
}