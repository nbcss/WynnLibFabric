package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty

class MantleResistanceProperty(ability: Ability,
                               private val resistance: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MantleResistanceProperty> {
        override fun create(ability: Ability, data: JsonElement): MantleResistanceProperty {
            return MantleResistanceProperty(ability, data.asInt)
        }
        override fun getKey(): String = "mantle_resistance"
    }

    fun getResistantBonus(): Int = resistance

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), resistance.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}