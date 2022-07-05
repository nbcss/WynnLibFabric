package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.CooldownTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.CooldownProperty

class Manachism(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    CooldownProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Manachism {
            return Manachism(parent, properties)
        }
    }
    private val cooldown: Double = CooldownProperty.read(json)

    override fun getCooldown(): Double = cooldown

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(CooldownTooltip)
    }
}