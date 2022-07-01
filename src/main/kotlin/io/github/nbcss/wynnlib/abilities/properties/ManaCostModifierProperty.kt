package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface ManaCostModifierProperty {
    companion object {
        const val KEY: String = "mana_modifier"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getManaModifier(): Int
}