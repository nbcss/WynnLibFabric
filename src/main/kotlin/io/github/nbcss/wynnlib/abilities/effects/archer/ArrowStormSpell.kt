package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty

class ArrowStormSpell(json: JsonObject): SpellUnlock(json), DamageProperty, RangeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): ArrowStormSpell {
            return ArrowStormSpell(properties)
        }
    }
    private val range: Double = RangeProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.read(json)

    override fun getRange(): Double = range

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip, RangeTooltip)
    }
}