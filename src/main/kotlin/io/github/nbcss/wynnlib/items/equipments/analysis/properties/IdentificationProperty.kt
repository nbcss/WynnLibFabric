package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.Identification
import net.minecraft.text.Text
import java.util.regex.Pattern

class IdentificationProperty: AnalysisProperty {
    companion object {
        private val ID_VALUE_PATTERN = Pattern.compile("(\\+\\d+|-\\d+)(.*)")
        const val KEY = "IDENTIFICATION"
    }
    private val idMap: MutableMap<Identification, Int> = mutableMapOf()
    private val starMap: MutableMap<Identification, Int> = mutableMapOf()
    private var terminated: Boolean = false //need this flag due to id from the set bonus...

    fun getIdentificationValue(id: Identification): Int {
        return idMap[id] ?: 0
    }

    fun getIdentificationStars(id: Identification): Int {
        return starMap[id] ?: 0
    }

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (terminated || tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        //stop further read stats if it from set bonus
        if (base.asString() == "Set Bonus:")
            terminated = true
        if (base.asString() != "" || base.siblings.size < 2)
            return 0
        val matcher = ID_VALUE_PATTERN.matcher(base.siblings[0].asString().trim())
        if (matcher.find()) {
            val value = matcher.group(1).toInt()
            val suffix = matcher.group(2)
            val idName = base.siblings.last().asString()
            Identification.fromSuffixName(suffix, idName)?.let { id ->
                idMap[id] = value
                if (base.siblings.size > 2){
                    val stars = base.siblings[1].asString().chars().filter { it.toChar() == '*' }.count()
                    starMap[id] = stars.toInt()
                }
                return 1
            }
        }
        return 0
    }

    override fun getKey(): String = KEY
}