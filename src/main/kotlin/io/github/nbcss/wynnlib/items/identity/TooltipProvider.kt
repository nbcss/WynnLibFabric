package io.github.nbcss.wynnlib.items.identity

import net.minecraft.text.Text

interface TooltipProvider {
    fun getTooltip(): List<Text>
}