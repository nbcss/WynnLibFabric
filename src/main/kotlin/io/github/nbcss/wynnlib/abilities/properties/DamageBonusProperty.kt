package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import net.minecraft.text.Text

interface DamageBonusProperty {
    companion object {
        const val KEY: String = "damage_bonus"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getDamageBonus(): Int

    fun getDamageBonusLabel(): Text?

    fun getDamageBonusRate(): Double = getDamageBonus() / 100.0
}