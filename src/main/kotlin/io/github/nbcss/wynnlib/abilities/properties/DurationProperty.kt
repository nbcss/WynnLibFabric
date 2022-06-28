package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement

object DurationProperty: AbilityProperty<Double> {
    const val KEY: String = "duration"

    override fun read(encoding: String): Double {
        return encoding.toDouble()
    }

    override fun write(data: JsonElement): String? {
        return data.asString
    }

    override fun getKey(): String = KEY
}