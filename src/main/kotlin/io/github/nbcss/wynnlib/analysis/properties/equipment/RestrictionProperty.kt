package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.data.Restriction
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting

class RestrictionProperty: AnalysisProperty {
    companion object {
        const val KEY = "RESTRICTION"
    }
    private var restriction: Restriction? = null

    fun getRestriction(): Restriction? = restriction

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if (base.style.color == TextColor.fromFormatting(Formatting.RED)) {
            Restriction.fromDisplayName(base.asString())?.let {
                restriction = it
                return 1
            }
        }
        return 0
    }

    override fun getKey(): String = KEY
}