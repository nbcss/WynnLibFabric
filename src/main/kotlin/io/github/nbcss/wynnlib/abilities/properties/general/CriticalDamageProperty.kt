package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

class CriticalDamageProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return CriticalDamageProperty(ability, data)
        }
        override fun getKey(): String = "critical_damage"
    }
    private val bonus: Int = data.asInt
    init {
        ability.putPlaceholder(getKey(), bonus.toString())
    }

    fun getCriticalDamageBonus(): Int = bonus
}