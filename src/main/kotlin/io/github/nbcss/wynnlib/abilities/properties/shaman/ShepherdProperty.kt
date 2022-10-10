package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty

class ShepherdProperty(ability: Ability,
                       private val increment: Int,
                       private val maxLimit: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<ShepherdProperty> {
        override fun create(ability: Ability, data: JsonElement): ShepherdProperty {
            val json = data.asJsonObject
            val inc = if (json.has(INC_KEY)) json[INC_KEY].asInt else 0
            val max = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
            return ShepherdProperty(ability, inc, max)
        }
        private const val INC_KEY = "inc"
        private const val MAX_KEY = "max"
        override fun getKey(): String = "shepherd"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("shepherd.inc", "$increment")
        container.putPlaceholder("shepherd.max", "$maxLimit")
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}