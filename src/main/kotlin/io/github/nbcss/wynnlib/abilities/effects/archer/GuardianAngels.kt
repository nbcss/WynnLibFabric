package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.*
import io.github.nbcss.wynnlib.registry.AbilityRegistry

class GuardianAngels(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DamageProperty, RangeProperty, DurationProperty, ArcherSentientBowsProperty, ReplacingProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): GuardianAngels {
            return GuardianAngels(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val duration: Double = DurationProperty.read(json)
    private val replacing: String = ReplacingProperty.read(json)
    private val bows: Int = ArcherSentientBowsProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getRange(): Double = range

    override fun getDuration(): Double = duration

    override fun getArcherSentientBows(): Int = bows

    override fun getReplacingAbility(): Ability? {
        return AbilityRegistry.get(replacing)
    }

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip, ArcherSentientBowsTooltip, RangeTooltip, DurationTooltip, ReplacingTooltip)
    }
}