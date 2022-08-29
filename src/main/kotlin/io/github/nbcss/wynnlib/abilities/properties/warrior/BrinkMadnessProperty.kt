package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class BrinkMadnessProperty(ability: Ability,
                           private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<BrinkMadnessProperty> {
        private const val RESISTANCE_KEY: String = "resistance"
        private const val HEALTH_KEY: String = "health"
        override fun create(ability: Ability, data: JsonElement): BrinkMadnessProperty {
            val json = data.asJsonObject
            val resistanceBonus = if (json.has(RESISTANCE_KEY)) json[RESISTANCE_KEY].asDouble else 0.0
            val health = if (json.has(HEALTH_KEY)) json[HEALTH_KEY].asDouble else 0.0
            return BrinkMadnessProperty(ability, Info(resistanceBonus, health))
        }
        override fun getKey(): String = "brink_madness"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("brink_madness.resistance", removeDecimal(info.resistanceBonus))
        container.putPlaceholder("brink_madness.health", removeDecimal(info.health))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val resistanceBonus: Double,
                    val health: Double)
}