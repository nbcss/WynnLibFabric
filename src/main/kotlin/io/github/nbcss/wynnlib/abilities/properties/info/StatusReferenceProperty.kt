package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

class StatusReferenceProperty(ability: Ability,
                              private val name: String): AbilityProperty(ability) {
    companion object: Type<StatusReferenceProperty> {
        override fun create(ability: Ability, data: JsonElement): StatusReferenceProperty {
            return StatusReferenceProperty(ability, data.asString)
        }
        override fun getKey(): String = "status_name"
    }

    fun getFooterName(): String = name
}