package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class CorruptedProperty(ability: Ability,
                        private val info: Info): AbilityProperty(ability), SetupProperty {
    companion object: Type<CorruptedProperty> {
        private const val CONVERT_KEY: String = "rate"
        private const val DAMAGE_BOOST_KEY: String = "damage_boost"
        private const val DAMAGE_BOOST_MAX_KEY: String = "max_boost"
        override fun create(ability: Ability, data: JsonElement): CorruptedProperty {
            val json = data.asJsonObject
            val convert = if (json.has(CONVERT_KEY)) json[CONVERT_KEY].asDouble else 0.0
            val boost = if (json.has(DAMAGE_BOOST_KEY)) json[DAMAGE_BOOST_KEY].asDouble else 0.0
            val maxBoost = if (json.has(DAMAGE_BOOST_MAX_KEY)) json[DAMAGE_BOOST_MAX_KEY].asDouble else 0.0
            return CorruptedProperty(ability, Info(convert, boost, maxBoost))
        }
        override fun getKey(): String = "corrupted"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("corrupted.rate", removeDecimal(info.convertRate))
        container.putPlaceholder("corrupted.damage_boost", removeDecimal(info.damageBonus))
        container.putPlaceholder("corrupted.max_boost", removeDecimal(info.damageBonusMax))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    class Modifier(ability: Ability,
                   private val modifier: Info):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                val json = data.asJsonObject
                val convert = if (json.has(CONVERT_KEY)) json[CONVERT_KEY].asDouble else 0.0
                val boost = if (json.has(DAMAGE_BOOST_KEY)) json[DAMAGE_BOOST_KEY].asDouble else 0.0
                val maxBoost = if (json.has(DAMAGE_BOOST_MAX_KEY)) json[DAMAGE_BOOST_MAX_KEY].asDouble else 0.0
                return Modifier(ability, Info(convert, boost, maxBoost))
            }
            override fun getKey(): String = "corrupted_modifier"
        }

        fun getModifier(): Info = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder("corrupted_modifier.rate", removeDecimal(modifier.convertRate))
            container.putPlaceholder("corrupted_modifier.damage_boost", removeDecimal(modifier.damageBonus))
            container.putPlaceholder("corrupted_modifier.max_boost", removeDecimal(modifier.damageBonusMax))
        }

        override fun modify(entry: PropertyEntry) {
            CorruptedProperty.from(entry)?.let {
                val info = it.getInfo().upgrade(getModifier())
                entry.setProperty(CorruptedProperty.getKey(), CorruptedProperty(it.getAbility(), info))
            }
        }
    }

    data class Info(val convertRate: Double,
                    val damageBonus: Double,
                    val damageBonusMax: Double) {
        fun upgrade(modifier: Info): Info {
            return Info(convertRate + modifier.convertRate,
                damageBonus + modifier.damageBonus,
                damageBonusMax + modifier.damageBonusMax)
        }
    }
}