package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.CostModifier
import io.github.nbcss.wynnlib.abilities.properties.legacy.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.BonusEffectProperty

class Ragnarokkr(parent: Ability, json: JsonObject): CostModifier(parent, json),
    AreaOfEffectProperty, BonusEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Ragnarokkr {
            return Ragnarokkr(parent, properties)
        }
    }
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostModifierTooltip, BonusEffectTooltip, AreaOfEffectTooltip.Modifier)
    }
}