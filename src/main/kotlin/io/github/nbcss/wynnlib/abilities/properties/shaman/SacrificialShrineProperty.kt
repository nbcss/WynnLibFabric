package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class SacrificialShrineProperty(ability: Ability,
                                private val cost: Double,
                                private val damageBoost: Double): AbilityProperty(ability), SetupProperty {
    companion object: Type<SacrificialShrineProperty> {
        private const val COST_KEY = "cost"
        private const val BOOST_KEY = "damage_boost"
        override fun create(ability: Ability, data: JsonElement): SacrificialShrineProperty {
            val json = data.asJsonObject
            val cost = if (json.has(COST_KEY)) json[COST_KEY].asDouble else 0.0
            val damageBoost = if (json.has(BOOST_KEY)) json[BOOST_KEY].asDouble else 0.0
            return SacrificialShrineProperty(ability, cost, damageBoost)
        }
        override fun getKey(): String = "sacrificial_shrine"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("sacrificial_shrine.cost", removeDecimal(cost))
        container.putPlaceholder("sacrificial_shrine.damage_boost", removeDecimal(damageBoost))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}