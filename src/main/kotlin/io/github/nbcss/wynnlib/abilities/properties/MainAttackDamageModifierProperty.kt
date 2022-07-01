package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject

interface MainAttackDamageModifierProperty {
    companion object {
        const val KEY: String = "main_damage_modifier"
        fun read(data: JsonObject): Int = if (data.has(KEY)) data[KEY].asInt else 0
    }

    fun getMainAttackDamageModifier(): Int

    fun getMainAttackDamageModifierRate(): Double = getMainAttackDamageModifier() / 100.0
}