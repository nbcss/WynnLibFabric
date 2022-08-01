package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

class ValuesProperty(ability: Ability,
                     private val values: Map<String, String>): AbilityProperty(ability) {
    companion object: Type<ValuesProperty> {
        override fun create(ability: Ability, data: JsonElement): ValuesProperty {
            val values: MutableMap<String, String> = mutableMapOf()
            for (entry in data.asJsonObject.entrySet()) {
                values[entry.key] = entry.value.asString
            }
            return ValuesProperty(ability, values)
        }
        override fun getKey(): String = "values"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        for (entry in values.entries) {
            container.putPlaceholder("values.${entry.key}", entry.value)
        }
    }
}