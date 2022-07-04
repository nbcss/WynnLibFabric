package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.CostModifier

class Provoke(parent: Ability, json: JsonObject): CostModifier(parent, json) {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Provoke {
            return Provoke(parent, properties)
        }
    }
    //todo
}