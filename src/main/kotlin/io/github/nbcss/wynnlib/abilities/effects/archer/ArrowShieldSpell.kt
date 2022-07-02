package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.ChargeProperty
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.DurationProperty

class ArrowShieldSpell(parent: Ability, json: JsonObject): SpellUnlock(parent, json),
    DamageProperty, DurationProperty, ChargeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): ArrowShieldSpell {
            return ArrowShieldSpell(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val duration: Double = DurationProperty.read(json)
    private val charges: Int = ChargeProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getDuration(): Double = duration

    override fun getCharges(): Int = charges

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip, ChargesTooltip, DurationTooltip)
    }
}