package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement

object RangeProperty: AbilityProperty<Double> {
    const val KEY: String = "range"

    override fun read(encoding: String): Double {
        return encoding.toDouble()
    }

    override fun write(data: JsonElement): String? {
        return data.asString
    }

    override fun getKey(): String = KEY
}