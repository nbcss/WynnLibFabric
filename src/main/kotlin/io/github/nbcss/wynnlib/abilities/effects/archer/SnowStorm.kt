package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.BonusEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class SnowStorm(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BonusEffectProperty, RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): SnowStorm {
            return SnowStorm(parent, properties)
        }
    }
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)
    private val range: Double = RangeProperty.read(json)

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getRange(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(BonusEffectTooltip, RangeTooltip)
    }
}