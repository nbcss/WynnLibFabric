package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class DiscombobulateProperty(ability: Ability,
                             private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<DiscombobulateProperty> {
        private const val CHARGE_RATE_KEY: String = "rate"
        private const val MAX_BOOST_KEY: String = "max"
        private const val DECAY_RATE_KEY: String = "decay"
        override fun create(ability: Ability, data: JsonElement): DiscombobulateProperty {
            val json = data.asJsonObject
            val rate = if (json.has(CHARGE_RATE_KEY)) json[CHARGE_RATE_KEY].asDouble else 0.0
            val maxBoost = if (json.has(MAX_BOOST_KEY)) json[MAX_BOOST_KEY].asDouble else 0.0
            val decay = if (json.has(DECAY_RATE_KEY)) json[DECAY_RATE_KEY].asDouble else 0.0
            return DiscombobulateProperty(ability, Info(rate, maxBoost, decay))
        }
        override fun getKey(): String = "discombobulate"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("discombobulate.rate", removeDecimal(info.rate))
        container.putPlaceholder("discombobulate.max", removeDecimal(info.damageBonusMax))
        container.putPlaceholder("discombobulate.decay", removeDecimal(info.decayRate))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val rate: Double,
                    val damageBonusMax: Double,
                    val decayRate: Double)
}