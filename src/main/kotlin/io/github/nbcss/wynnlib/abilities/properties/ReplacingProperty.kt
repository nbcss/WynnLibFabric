package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability

interface ReplacingProperty {
    companion object {
        const val KEY: String = "replacing"
        fun read(data: JsonObject): String = if (data.has(KEY)) data[KEY].asString else ""
    }

    fun getReplacingAbility(): Ability?
}