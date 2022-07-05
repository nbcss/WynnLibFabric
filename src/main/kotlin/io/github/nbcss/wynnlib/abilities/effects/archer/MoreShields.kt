package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.ArcherSentientBowsProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.ChargeProperty

class MoreShields(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    ChargeProperty, ArcherSentientBowsProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): MoreShields {
            return MoreShields(parent, properties)
        }
    }
    private val charges: Int = ChargeProperty.read(json)
    private val bows: Int = ArcherSentientBowsProperty.read(json)

    override fun getCharges(): Int = charges

    override fun getArcherSentientBows(): Int = bows

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ArcherSentientBowsTooltip.Modifier, ChargesTooltip.Modifier)
    }
}