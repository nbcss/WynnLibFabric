package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

class ResistantBonusProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ResistantBonusProperty(ability, data)
        }
        override fun getKey(): String = "resistant_bonus"
    }
    private val bonus: Int = data.asInt
    init {
        ability.putPlaceholder(getKey(), bonus.toString())
    }

    fun getResistantBonus(): Int = bonus

    fun getResistantBonusRate(): Double = getResistantBonus() / 100.0
}