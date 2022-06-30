package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface RangeProperty {
    companion object {
        const val KEY: String = "range"
        fun read(data: JsonObject): Double = if (data.has(KEY)) data[KEY].asDouble else 0.0
    }

    fun getRange(): Double
}