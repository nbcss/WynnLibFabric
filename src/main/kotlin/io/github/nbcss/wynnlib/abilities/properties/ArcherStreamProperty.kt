package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface ArcherStreamProperty {
    companion object {
        const val KEY: String = "archer_stream"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getArcherStreams(): Int
}