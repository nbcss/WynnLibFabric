package io.github.nbcss.wynnlib

import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.abs

object Settings {
    private val colorMap: MutableMap<String, Int> = LinkedHashMap()
    init {
        colorMap["tier.mythic"] = 0xAA00AA
        colorMap["tier.fabled"] = 0xFF5555
        colorMap["tier.legendary"] = 0x55FFFF
        colorMap["tier.rare"] = 0xFF55FF
        colorMap["tier.unique"] = 0xFFFF55
        colorMap["tier.set"] = 0x55FF55
        colorMap["tier.normal"] = 0xFFFFFF
        colorMap["tier.crafted"] = 0x00AAAA
        colorMap["ingredient_tier.star_0"] = 0x555555
        colorMap["ingredient_tier.star_1"] = 0xFFFF55
        colorMap["ingredient_tier.star_2"] = 0xFF55FF
        colorMap["ingredient_tier.star_3"] = 0x55FFFF
        colorMap["powder_tier.i"] = 0xFFFFFF
        colorMap["powder_tier.ii"] = 0xFFFF55
        colorMap["powder_tier.iii"] = 0xFF55FF
        colorMap["powder_tier.iv"] = 0x55FFFF
        colorMap["powder_tier.v"] = 0xFF5555
        colorMap["powder_tier.vi"] = 0xAA00AA
    }
    fun getColor(prefix: String, label: String): Int {
        val key = "${prefix}.$label".lowercase(Locale.getDefault())
        return colorMap.getOrDefault(key, 0)
    }
}