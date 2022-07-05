package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject

interface CooldownProperty {
    companion object {
        const val KEY: String = "cooldown"
        fun read(data: JsonObject): Double = if (data.has(KEY)) data[KEY].asDouble else 0.0
    }

    fun getCooldown(): Double
}