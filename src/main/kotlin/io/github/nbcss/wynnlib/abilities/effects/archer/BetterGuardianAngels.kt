package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier

class BetterGuardianAngels(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json) {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BetterGuardianAngels {
            return BetterGuardianAngels(parent, properties)
        }
    }

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier)
    }
}