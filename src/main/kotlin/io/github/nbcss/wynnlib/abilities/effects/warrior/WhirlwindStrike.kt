package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.RangeTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class WhirlwindStrike(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    AreaOfEffectProperty, RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): WhirlwindStrike {
            return WhirlwindStrike(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)

    override fun getRange(): Double = range

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, AreaOfEffectTooltip.Modifier, RangeTooltip.Modifier)
    }
}