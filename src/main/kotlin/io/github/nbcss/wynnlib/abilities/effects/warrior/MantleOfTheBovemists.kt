package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.ChargeProperty
import io.github.nbcss.wynnlib.abilities.properties.CooldownProperty
import io.github.nbcss.wynnlib.abilities.properties.DurationProperty

class MantleOfTheBovemists(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    ChargeProperty, CooldownProperty, DurationProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): MantleOfTheBovemists {
            return MantleOfTheBovemists(parent, properties)
        }
    }
    private val charges: Int = ChargeProperty.read(json)
    private val cooldown: Double = CooldownProperty.read(json)
    private val duration: Double = DurationProperty.read(json)

    override fun getCharges(): Int = charges

    override fun getCooldown(): Double = cooldown

    override fun getDuration(): Double = duration

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DurationTooltip, CooldownTooltip, ChargesTooltip)
    }
}