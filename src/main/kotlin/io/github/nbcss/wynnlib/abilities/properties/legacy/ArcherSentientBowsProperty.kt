package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject

interface ArcherSentientBowsProperty {
    companion object {
        const val KEY: String = "archer_sentient_bows"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getArcherSentientBows(): Int
}