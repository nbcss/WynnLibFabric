package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty

class HeavyImpact(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DamageProperty, AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): HeavyImpact {
            return HeavyImpact(parent, properties)
        }
    }
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip, AreaOfEffectTooltip)
    }
}