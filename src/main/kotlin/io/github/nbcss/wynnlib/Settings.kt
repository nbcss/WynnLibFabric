package io.github.nbcss.wynnlib

import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.abs

object Settings {
    private val colorMap: MutableMap<String, Int> = LinkedHashMap()
    init {
        colorMap["tier_mythic"] = 0xAA00AA
        colorMap["tier_fabled"] = 0xFF5555
        colorMap["tier_legendary"] = 0x55FFFF
        colorMap["tier_rare"] = 0xFF55FF
        colorMap["tier_unique"] = 0xFFFF55
        colorMap["tier_set"] = 0x55FF55
        colorMap["tier_normal"] = 0xFFFFFF
        colorMap["tier_crafted"] = 0x00AAAA
    }
    fun getColor(label: String): Int {
        return colorMap.getOrDefault(label.lowercase(Locale.getDefault()), 0)
    }
}