package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject

interface ResistantBonusProperty {
    companion object {
        const val KEY: String = "resistant_bonus"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getResistantBonus(): Int

    fun getResistantBonusRate(): Double = getResistantBonus() / 100.0
}