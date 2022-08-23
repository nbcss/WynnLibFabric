package io.github.nbcss.wynnlib.analysis.properties

import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

interface AnalysisProperty: Keyed {
    fun set(tooltip: List<Text>, line: Int): Int
}