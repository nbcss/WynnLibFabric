package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class EnragedBlowProperty(ability: Ability,
                          private val info: Info): AbilityProperty(ability), SetupProperty {
    companion object: Type<EnragedBlowProperty> {
        private const val CONVERT_KEY: String = "rate"
        private const val DAMAGE_BOOST_KEY: String = "damage_boost"
        private const val DAMAGE_BOOST_MAX_KEY: String = "max_boost"
        override fun create(ability: Ability, data: JsonElement): EnragedBlowProperty {
            val json = data.asJsonObject
            val convert = if (json.has(CONVERT_KEY)) json[CONVERT_KEY].asDouble else 0.0
            val boost = if (json.has(DAMAGE_BOOST_KEY)) json[DAMAGE_BOOST_KEY].asDouble else 0.0
            val maxBoost = if (json.has(DAMAGE_BOOST_MAX_KEY)) json[DAMAGE_BOOST_MAX_KEY].asDouble else 0.0
            return EnragedBlowProperty(ability, Info(convert, boost, maxBoost))
        }
        override fun getKey(): String = "enraged_blow"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("enraged_blow.rate", removeDecimal(info.convertRate))
        container.putPlaceholder("enraged_blow.damage_boost", removeDecimal(info.damageBonus))
        container.putPlaceholder("enraged_blow.max_boost", removeDecimal(info.damageBonusMax))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val convertRate: Double,
                    val damageBonus: Double,
                    val damageBonusMax: Double)
}