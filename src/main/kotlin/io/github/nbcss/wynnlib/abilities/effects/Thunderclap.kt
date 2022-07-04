package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.AreaOfEffectTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.properties.AreaOfEffectProperty

class Thunderclap(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    AreaOfEffectProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Thunderclap {
            return Thunderclap(parent, properties)
        }
    }
    private val aoe: AreaOfEffectProperty.AreaOfEffect = AreaOfEffectProperty.read(json)

    override fun getAreaOfEffect(): AreaOfEffectProperty.AreaOfEffect = aoe

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(AreaOfEffectTooltip.Modifier)
    }
}