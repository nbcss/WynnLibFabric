package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.RangeTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class StrongerHook(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): StrongerHook {
            return StrongerHook(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)

    override fun getRange(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(RangeTooltip.Modifier)
    }
}