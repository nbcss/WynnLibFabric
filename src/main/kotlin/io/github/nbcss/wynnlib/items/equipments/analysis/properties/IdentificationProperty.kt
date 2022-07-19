package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.Identification
import net.minecraft.text.Text

class IdentificationProperty: ItemProperty {
    companion object {
        const val KEY = "IDENTIFICATION"
    }
    private val idMap: MutableMap<Identification, Int> = mutableMapOf()

    override fun set(tooltip: List<Text>, line: Int): Int {
        return 0
    }

    fun getIdentificationValue(id: Identification): Int {
        return idMap[id] ?: 0
    }

    override fun getKey(): String = KEY
}