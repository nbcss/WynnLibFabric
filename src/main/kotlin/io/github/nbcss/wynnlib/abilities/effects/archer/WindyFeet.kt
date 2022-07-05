package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.BonusEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DurationProperty

class WindyFeet(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    BonusEffectProperty, DurationProperty, AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): WindyFeet {
            return WindyFeet(parent, properties)
        }
    }
    private val duration: Double = DurationProperty.read(json)
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)

    override fun getDuration(): Double = duration

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(BonusEffectTooltip, DurationTooltip, AreaOfEffectTooltip)
    }
}