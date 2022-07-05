package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ManaCostTooltip
import io.github.nbcss.wynnlib.abilities.properties.legacy.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.ManaCostProperty
import io.github.nbcss.wynnlib.data.SpellSlot

open class SpellUnlock(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty, ManaCostProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): AbilityEffect {
            return SpellUnlock(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val mana: Int = ManaCostProperty.read(json)

    override fun getSpell(): SpellSlot = spell

    override fun getManaCost(): Int = mana

    override fun getTooltipItems(): List<EffectTooltip> = listOf(ManaCostTooltip)
}