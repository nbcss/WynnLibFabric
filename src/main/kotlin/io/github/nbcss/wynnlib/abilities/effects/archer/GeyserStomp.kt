package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageProperty

class GeyserStomp(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DamageProperty, AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): GeyserStomp {
            return GeyserStomp(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readModifier(json)
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, AreaOfEffectTooltip.Modifier)
    }
}