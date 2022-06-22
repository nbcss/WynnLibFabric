package io.github.nbcss.wynnlib.abilities.tips

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import net.minecraft.text.Text

interface EffectTip {
    companion object {
        private val FACTORY_MAP: Map<String, Factory> = mapOf(
            "MANA_COST" to ManaCostTip.FACTORY
        )

        fun fromData(json: JsonObject): EffectTip? {
            return FACTORY_MAP[json["type"].asString.uppercase()]?.create(json)
        }
    }

    fun addTip(ability: Ability, tooltip: MutableList<Text>)

    interface Factory {
        fun create(json: JsonObject): EffectTip?
    }
}