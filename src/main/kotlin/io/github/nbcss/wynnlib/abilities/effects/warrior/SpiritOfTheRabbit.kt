package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.CostModifier

class SpiritOfTheRabbit(parent: Ability, json: JsonObject): CostModifier(parent, json) {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): SpiritOfTheRabbit {
            return SpiritOfTheRabbit(parent, properties)
        }
    }
}