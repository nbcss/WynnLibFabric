package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.DurationTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.*
import io.github.nbcss.wynnlib.data.SpellSlot

class FireCreep(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty, DamageProperty, DurationProperty, AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): FireCreep {
            return FireCreep(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)
    private val duration: Double = DurationProperty.read(json)

    override fun getSpell(): SpellSlot = spell

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getDuration(): Double = duration

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip, DurationTooltip, AreaOfEffectTooltip)
    }
}