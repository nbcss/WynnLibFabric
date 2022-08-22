package io.github.nbcss.wynnlib.analysis.properties

import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

interface AnalysisProperty: Keyed, TooltipProvider {
    fun set(tooltip: List<Text>, line: Int): Int
}