package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface DurationProperty {
    companion object {
        const val KEY: String = "duration"
        fun read(data: JsonObject): Double = data[KEY].asDouble
    }

    fun getDuration(): Double
}