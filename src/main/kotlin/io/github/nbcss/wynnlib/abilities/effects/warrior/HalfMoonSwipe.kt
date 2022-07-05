package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ManaCostModifierTooltip
import io.github.nbcss.wynnlib.abilities.display.RangeTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.legacy.ManaCostModifierProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.RangeProperty

class HalfMoonSwipe(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    RangeProperty, ManaCostModifierProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): HalfMoonSwipe {
            return HalfMoonSwipe(parent, properties)
        }
    }
    private val modifier: Int = ManaCostModifierProperty.read(json)
    private val range: Double = RangeProperty.read(json)

    override fun getManaModifier(): Int = modifier

    override fun getRange(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostModifierTooltip, DamageTooltip.Modifier, RangeTooltip.Modifier)
    }
}