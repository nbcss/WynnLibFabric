package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class BashSpell(json: JsonObject): SpellUnlock(json), DamageProperty, AreaOfEffectProperty, RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): BashSpell {
            return BashSpell(properties)
        }
    }
    private val range: Double = RangeProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.read(json)
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)

    override fun getRange(): Double = range

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip, AreaOfEffectTooltip, RangeTooltip)
    }
}