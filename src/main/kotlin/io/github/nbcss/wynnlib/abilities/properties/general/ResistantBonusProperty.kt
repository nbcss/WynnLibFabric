package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

class ResistantBonusProperty(ability: Ability,
                             private val bonus: Int): AbilityProperty(ability) {
    companion object: Type<ResistantBonusProperty> {
        override fun create(ability: Ability, data: JsonElement): ResistantBonusProperty {
            return ResistantBonusProperty(ability, data.asInt)
        }
        override fun getKey(): String = "resistant_bonus"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), bonus.toString())
    }

    fun getResistantBonus(): Int = bonus

    fun getResistantBonusRate(): Double = getResistantBonus() / 100.0
}