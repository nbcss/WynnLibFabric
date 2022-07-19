package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.Identification
import net.minecraft.text.Text
import java.util.regex.Pattern

class IdentificationProperty: ItemProperty {
    companion object {
        private val ID_VALUE_PATTERN = Pattern.compile("(\\+\\d+|-\\d+)(.*)")
        const val KEY = "IDENTIFICATION"
    }
    private val idMap: MutableMap<Identification, Int> = mutableMapOf()

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if (base.asString() != "" || base.siblings.size < 2)
            return 0
        val matcher = ID_VALUE_PATTERN.matcher(base.siblings[0].asString().trim())
        if (matcher.find()) {
            //todo did not parse star yet
            val value = matcher.group(1).toInt()
            val suffix = matcher.group(2)
            val idName = base.siblings.last().asString()
            Identification.fromSuffixName(suffix, idName)?.let {
                idMap[it] = value
                return 1
            }
        }
        return 0
    }

    fun getIdentificationValue(id: Identification): Int {
        return idMap[id] ?: 0
    }

    override fun getKey(): String = KEY
}