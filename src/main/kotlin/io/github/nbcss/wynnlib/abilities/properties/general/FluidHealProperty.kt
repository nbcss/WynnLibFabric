package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class FluidHealProperty(ability: Ability,
                        private val power: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<FluidHealProperty> {
        override fun create(ability: Ability, data: JsonElement): FluidHealProperty {
            return FluidHealProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "fluid_heal"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), removeDecimal(power))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

}