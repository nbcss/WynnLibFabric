package io.github.nbcss.wynnlib.abilities.effects.spells.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.spells.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.ChargeProperty
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.DurationProperty

class ArrowShieldSpell(json: JsonObject): SpellUnlock(json), DamageProperty, DurationProperty, ChargeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): ArrowShieldSpell {
            return ArrowShieldSpell(properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.read(json)
    private val duration: Double = DurationProperty.read(json)
    private val charges: Int = ChargeProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getDuration(): Double = duration

    override fun getCharges(): Int = charges

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip, ChargesTooltip, DurationTooltip)
    }
}