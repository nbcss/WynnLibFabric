package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.BonusEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.CooldownProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DurationProperty

class BoilingBlood(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DamageProperty, BonusEffectProperty, DurationProperty, CooldownProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BoilingBlood {
            return BoilingBlood(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val cooldown: Double = CooldownProperty.read(json)
    private val duration: Double = DurationProperty.read(json)
    private val effect: BonusEffectProperty.EffectBonus = BonusEffectProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getDuration(): Double = duration

    override fun getEffectBonus(): BonusEffectProperty.EffectBonus = effect

    override fun getCooldown(): Double = cooldown

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip, CooldownTooltip, DurationTooltip, BonusEffectTooltip)
    }
}