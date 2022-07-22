package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.Identification

class IDModifierProperty(ability: Ability,
                         pairs: List<Pair<Identification, Int>>):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<IDModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): IDModifierProperty {
            val json = data.asJsonObject
            val values: MutableList<Pair<Identification, Int>> = mutableListOf()
            for (entry in json.entrySet()) {
                Identification.fromName(entry.key)?.let {
                    val value = entry.value.asInt
                    if (value != 0)
                        values.add(it to value)
                }
            }
            return IDModifierProperty(ability, values)
        }
        override fun getKey(): String = "id_modifier"
    }

    private val modifiers: Map<Identification, Int> = mapOf(pairs = pairs.toTypedArray())

    fun getModifiers(): Map<Identification, Int> = modifiers

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}