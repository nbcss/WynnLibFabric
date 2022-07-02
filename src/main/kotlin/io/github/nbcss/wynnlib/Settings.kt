package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Color
import java.util.*
import kotlin.collections.LinkedHashMap

object Settings {
    private val colorMap: MutableMap<String, Color> = LinkedHashMap()
    init {
        colorMap["tier.mythic"] = Color.DARK_PURPLE
        colorMap["tier.fabled"] = Color.RED
        colorMap["tier.legendary"] = Color.AQUA
        colorMap["tier.rare"] = Color.PINK
        colorMap["tier.unique"] = Color.YELLOW
        colorMap["tier.set"] = Color.GREEN
        colorMap["tier.normal"] = Color.WHITE
        colorMap["tier.crafted"] = Color.DARK_AQUA
        colorMap["ingredient_tier.star_0"] = Color.DARK_GRAY
        colorMap["ingredient_tier.star_1"] = Color.YELLOW
        colorMap["ingredient_tier.star_2"] = Color.PINK
        colorMap["ingredient_tier.star_3"] = Color.AQUA
        colorMap["material_tier.star_1"] = Color.YELLOW
        colorMap["material_tier.star_2"] = Color.PINK
        colorMap["material_tier.star_3"] = Color.AQUA
        colorMap["powder_tier.i"] = Color.WHITE
        colorMap["powder_tier.ii"] = Color.YELLOW
        colorMap["powder_tier.iii"] = Color.PINK
        colorMap["powder_tier.iv"] = Color.AQUA
        colorMap["powder_tier.v"] = Color.RED
        colorMap["powder_tier.vi"] = Color.DARK_PURPLE
    }

    fun getTierColor(tier: Tier): Color{
        return getColor("tier", tier.name)
    }

    fun getColor(prefix: String, label: String): Color {
        val key = "${prefix}.$label".lowercase()
        return colorMap.getOrDefault(key, Color.WHITE)
    }
}