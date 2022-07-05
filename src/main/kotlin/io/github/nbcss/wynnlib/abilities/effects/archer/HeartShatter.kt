package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier

class HeartShatter(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json) {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): HeartShatter {
            return HeartShatter(parent, properties)
        }
    }
}