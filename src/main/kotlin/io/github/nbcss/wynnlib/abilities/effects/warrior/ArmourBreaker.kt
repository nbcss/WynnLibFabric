package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.BonusEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DurationTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.BonusEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.DurationProperty

class ArmourBreaker(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    BonusEffectProperty, DurationProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): ArmourBreaker {
            return ArmourBreaker(parent, properties)
        }
    }
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)
    private val duration: Double = DurationProperty.read(json)

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getDuration(): Double = duration

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(BonusEffectTooltip, DurationTooltip)
    }
}