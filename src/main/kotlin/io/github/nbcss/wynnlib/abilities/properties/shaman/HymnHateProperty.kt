package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class HymnHateProperty(ability: Ability,
                       private val damage: Double): AbilityProperty(ability), SetupProperty {
    companion object: Type<HymnHateProperty> {
        override fun create(ability: Ability, data: JsonElement): HymnHateProperty {
            return HymnHateProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "hymn_of_hate"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), removeDecimal(damage))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}