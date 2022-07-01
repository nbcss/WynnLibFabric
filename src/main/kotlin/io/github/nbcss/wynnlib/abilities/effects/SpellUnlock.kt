package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ManaCostTooltip
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.ManaCostProperty
import io.github.nbcss.wynnlib.data.SpellSlot

open class SpellUnlock(json: JsonObject): BaseEffect(json),
    BoundSpellProperty, ManaCostProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): AbilityEffect {
            return SpellUnlock(properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val mana: Int = ManaCostProperty.read(json)

    override fun getSpell(): SpellSlot = spell

    override fun getManaCost(): Int = mana

    override fun getTooltipItems(): List<EffectTooltip> = listOf(ManaCostTooltip)
}