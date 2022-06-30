package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface RangeProperty {
    companion object {
        const val KEY: String = "range"
        fun read(data: JsonObject): Double = data[KEY].asDouble
    }

    fun getRange(): Double
}