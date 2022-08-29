package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class MassiveBashProperty(ability: Ability,
                          private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MassiveBashProperty> {
        private const val CONVERT_KEY: String = "rate"
        private const val RANGE_BOOST_KEY: String = "range_boost"
        private const val RANGE_BOOST_MAX_KEY: String = "max_boost"
        override fun create(ability: Ability, data: JsonElement): MassiveBashProperty {
            val json = data.asJsonObject
            val convert = if (json.has(CONVERT_KEY)) json[CONVERT_KEY].asDouble else 0.0
            val boost = if (json.has(RANGE_BOOST_KEY)) json[RANGE_BOOST_KEY].asDouble else 0.0
            val maxBoost = if (json.has(RANGE_BOOST_MAX_KEY)) json[RANGE_BOOST_MAX_KEY].asDouble else 0.0
            return MassiveBashProperty(ability, Info(convert, boost, maxBoost))
        }
        override fun getKey(): String = "massive_bash"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("massive_bash.rate", removeDecimal(info.convertRate))
        container.putPlaceholder("massive_bash.range_boost", removeDecimal(info.rangeBonus))
        container.putPlaceholder("massive_bash.max_boost", removeDecimal(info.rangeBonusMax))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val convertRate: Double,
                    val rangeBonus: Double,
                    val rangeBonusMax: Double)
}