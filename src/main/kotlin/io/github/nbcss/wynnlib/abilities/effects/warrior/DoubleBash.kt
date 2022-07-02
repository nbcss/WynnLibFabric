package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty
import io.github.nbcss.wynnlib.data.SpellSlot

class DoubleBash(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty, DamageProperty, RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): DoubleBash {
            return DoubleBash(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val range: Double = RangeProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.readModifier(json)

    override fun getSpell(): SpellSlot = spell

    override fun getRange(): Double = range

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageModifierTooltip, RangeModifierTooltip)
    }
}