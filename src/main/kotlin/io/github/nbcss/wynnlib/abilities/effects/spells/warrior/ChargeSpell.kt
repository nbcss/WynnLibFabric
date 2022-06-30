package io.github.nbcss.wynnlib.abilities.effects.spells.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.spells.SpellUnlock

class ChargeSpell(json: JsonObject): SpellUnlock(json) {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): ChargeSpell {
            return ChargeSpell(properties)
        }
    }
}