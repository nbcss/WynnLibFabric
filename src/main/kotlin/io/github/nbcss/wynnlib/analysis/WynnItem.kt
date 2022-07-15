package io.github.nbcss.wynnlib.analysis

import net.minecraft.text.Text

interface WynnItem {
    fun getAnalysisTooltip(): List<Text>
}