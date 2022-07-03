package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.RangeTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class AllSeeingPanoptes(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): AllSeeingPanoptes {
            return AllSeeingPanoptes(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)

    override fun getRange(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, RangeTooltip.Modifier)
    }
}