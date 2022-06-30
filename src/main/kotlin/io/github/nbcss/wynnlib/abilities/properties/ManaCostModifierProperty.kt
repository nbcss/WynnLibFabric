package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface ManaCostModifierProperty {
    companion object {
        const val KEY: String = "mana_modifier"
        fun read(data: JsonObject): Int = data[KEY].asInt
    }

    fun getManaModifier(): Int
}