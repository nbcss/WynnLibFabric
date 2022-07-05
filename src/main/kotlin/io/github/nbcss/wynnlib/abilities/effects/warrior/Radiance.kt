package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.BonusEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.CooldownProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DurationProperty

class Radiance(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DurationProperty, BonusEffectProperty, CooldownProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Radiance {
            return Radiance(parent, properties)
        }
    }
    private val duration: Double = DurationProperty.read(json)
    private val cooldown: Double = CooldownProperty.read(json)
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getDuration(): Double = duration

    override fun getCooldown(): Double = cooldown

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(BonusEffectTooltip, DurationTooltip, CooldownTooltip)
    }
}