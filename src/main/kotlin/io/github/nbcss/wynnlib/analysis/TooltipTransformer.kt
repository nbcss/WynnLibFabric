package io.github.nbcss.wynnlib.analysis

import net.minecraft.text.Text

interface TooltipTransformer {
    fun getTransformedTooltip(): List<Text>

    companion object {

    }
}