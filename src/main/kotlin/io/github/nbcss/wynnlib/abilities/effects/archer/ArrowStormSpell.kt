package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.*
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.legacy.ArcherStreamProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.RangeProperty

class ArrowStormSpell(parent: Ability, json: JsonObject): SpellUnlock(parent, json),
    DamageProperty, RangeProperty, ArcherStreamProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): ArrowStormSpell {
            return ArrowStormSpell(parent, properties)
        }
    }
    private val range: Double = RangeProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val streams: Int = ArcherStreamProperty.read(json)

    override fun getRange(): Double = range

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getArcherStreams(): Int = streams

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip, ArcherStreamTooltip, RangeTooltip)
    }
}