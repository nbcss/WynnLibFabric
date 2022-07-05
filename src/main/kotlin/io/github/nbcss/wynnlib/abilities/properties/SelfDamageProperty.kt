package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability

class SelfDamageProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return SelfDamageProperty(ability, data)
        }
        override fun getKey(): String = "self_damage"
    }
    private val damage: Int = data.asInt
    init {
        ability.putPlaceholder(getKey(), damage.toString())
    }

    fun getSelfDamage(): Int = damage
}