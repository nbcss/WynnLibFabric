package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface MaxProperty {
    companion object {
        const val KEY: String = "max"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getMaxValue(): Int
}