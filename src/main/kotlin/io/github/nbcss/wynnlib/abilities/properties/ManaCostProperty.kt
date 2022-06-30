package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface ManaCostProperty {
    companion object {
        const val KEY: String = "mana_cost"
        fun read(data: JsonObject): Int = data[KEY].asInt
    }

    fun getManaCost(): Int
}