package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject

interface MainAttackRangeModifierProperty {
    companion object {
        const val KEY: String = "main_range_modifier"
        fun read(data: JsonObject): Double = if (data.has(KEY)) data[KEY].asDouble else 0.0
    }

    fun getMainAttackRangeModifier(): Double
}