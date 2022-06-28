package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement

object ManaCostProperty: AbilityProperty<Int> {
    const val KEY: String = "mana_cost"

    override fun read(encoding: String): Int {
        return encoding.toInt()
    }

    override fun write(data: JsonElement): String? {
        return data.asString
    }

    override fun getKey(): String = KEY
}