package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.legacy.AreaOfEffectProperty

class BetterArrowShield(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BetterArrowShield {
            return BetterArrowShield(parent, properties)
        }
    }
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, AreaOfEffectTooltip.Modifier)
    }
}