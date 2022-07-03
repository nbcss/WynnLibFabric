package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.ArcherSentientBowsProperty
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.ChargeProperty
import io.github.nbcss.wynnlib.data.SpellSlot

class MoreShields(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty, ChargeProperty, ArcherSentientBowsProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): MoreShields {
            return MoreShields(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val charges: Int = ChargeProperty.read(json)
    private val bows: Int = ArcherSentientBowsProperty.read(json)

    override fun getSpell(): SpellSlot = spell

    override fun getCharges(): Int = charges

    override fun getArcherSentientBows(): Int = bows

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ArcherSentientBowsTooltip.Modifier, ChargesTooltip.Modifier)
    }
}