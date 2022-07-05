package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.ChargesTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BoundSpellEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.ChargeProperty

class StrongerMantle(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    ChargeProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): StrongerMantle {
            return StrongerMantle(parent, properties)
        }
    }
    private val charges: Int = ChargeProperty.read(json)

    override fun getCharges(): Int = charges

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ChargesTooltip.Modifier)
    }
}