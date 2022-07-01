package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ManaCostModifierTooltip
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.ManaCostModifierProperty
import io.github.nbcss.wynnlib.data.SpellSlot

class CostModifier(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty, ManaCostModifierProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): CostModifier {
            return CostModifier(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val modifier: Int = ManaCostModifierProperty.read(json)

    override fun getSpell(): SpellSlot = spell

    override fun getManaModifier(): Int = modifier

    override fun getTooltipItems(): List<EffectTooltip> = listOf(ManaCostModifierTooltip)
}