package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.*

class TwainArc(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    DamageProperty, RangeProperty, CooldownProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): TwainArc {
            return TwainArc(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val range: Double = RangeProperty.read(json)
    private val cooldown: Double = CooldownProperty.read(json)

    override fun getRange(): Double = range

    override fun getCooldown(): Double = cooldown

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip, RangeTooltip, CooldownTooltip)
    }
}