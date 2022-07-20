package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.Restriction
import net.minecraft.text.Text

class RestrictionProperty: AnalysisProperty {
    companion object {
        const val KEY = "RESTRICTION"
    }
    private var restriction: Restriction? = null

    fun getRestriction(): Restriction? = restriction

    override fun set(tooltip: List<Text>, line: Int): Int {
        //todo
        return 0
    }

    override fun getKey(): String = KEY
}