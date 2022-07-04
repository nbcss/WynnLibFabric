package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class BashUpgrade(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BashUpgrade {
            return BashUpgrade(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)

    override fun getRange(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, RangeTooltip.Modifier)
    }
}