package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.CooldownTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect

class BetterLeap(parent: Ability, json: JsonObject): Leap(parent, json) {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BetterLeap {
            return BetterLeap(parent, properties)
        }
    }

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(CooldownTooltip.Modifier)
    }
}