package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ElementBoosterTooltip
import io.github.nbcss.wynnlib.abilities.properties.ElementBoosterProperty

class ElementMastery(json: JsonObject): BaseEffect(json), ElementBoosterProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): ElementMastery {
            return ElementMastery(properties)
        }
    }
    private val booster: ElementBoosterProperty.Booster = ElementBoosterProperty.read(json)

    override fun getElementBooster(): ElementBoosterProperty.Booster = booster

    override fun getTooltipItems(): List<EffectTooltip> = listOf(ElementBoosterTooltip)
}