package io.github.nbcss.wynnlib.abilities.properties.archer

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class DecimatorProperty(ability: Ability,
                        private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<DecimatorProperty> {
        private const val DAMAGE_BOOST_KEY: String = "damage_boost"
        private const val DAMAGE_BOOST_MAX_KEY: String = "max_boost"
        override fun create(ability: Ability, data: JsonElement): DecimatorProperty {
            val json = data.asJsonObject
            val boost = if (json.has(DAMAGE_BOOST_KEY)) json[DAMAGE_BOOST_KEY].asDouble else 0.0
            val maxBoost = if (json.has(DAMAGE_BOOST_MAX_KEY)) json[DAMAGE_BOOST_MAX_KEY].asDouble else 0.0
            return DecimatorProperty(ability, Info(boost, maxBoost))
        }
        override fun getKey(): String = "decimator"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("decimator.damage_boost", removeDecimal(info.damageBonus))
        container.putPlaceholder("decimator.max_boost", removeDecimal(info.damageBonusMax))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val damageBonus: Double,
                    val damageBonusMax: Double)
}