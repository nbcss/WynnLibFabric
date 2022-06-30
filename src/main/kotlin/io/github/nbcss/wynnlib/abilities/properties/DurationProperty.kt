package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface DurationProperty {
    companion object {
        const val KEY: String = "duration"
        fun read(data: JsonObject): Double = if (data.has(KEY)) data[KEY].asDouble else 0.0
    }

    fun getDuration(): Double
}