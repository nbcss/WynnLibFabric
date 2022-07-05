package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject

interface ChargeProperty {
    companion object {
        const val KEY: String = "charges"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getCharges(): Int
}