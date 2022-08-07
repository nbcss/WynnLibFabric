package io.github.nbcss.wynnlib.abilities.properties.archer

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty

class PatientHunterProperty(ability: Ability,
                            private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<PatientHunterProperty> {
        private const val SEC_BOOST_KEY = "sec_boost"
        private const val MAX_BOOST_KEY = "max_boost"
        override fun create(ability: Ability, data: JsonElement): PatientHunterProperty {
            val json = data.asJsonObject
            val secBoost = if (json.has(SEC_BOOST_KEY)) json[SEC_BOOST_KEY].asInt else 0
            val maxBoost = if (json.has(MAX_BOOST_KEY)) json[MAX_BOOST_KEY].asInt else 0
            return PatientHunterProperty(ability, Info(secBoost, maxBoost))
        }
        override fun getKey(): String = "patient_hunter"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("patient_hunter.sec_boost", info.damageBonusSec.toString())
        container.putPlaceholder("patient_hunter.max_boost", info.damageBonusMax.toString())
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
                val secBoost = if (json.has(SEC_BOOST_KEY)) json[SEC_BOOST_KEY].asInt else 0
                val maxBoost = if (json.has(MAX_BOOST_KEY)) json[MAX_BOOST_KEY].asInt else 0
                return Modifier(ability, Info(secBoost, maxBoost))
            }
            override fun getKey(): String = "patient_hunter_modifier"
        }

        fun getModifier(): Info = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder("patient_hunter_modifier.sec_boost", modifier.damageBonusSec.toString())
            container.putPlaceholder("patient_hunter_modifier.max_boost", modifier.damageBonusMax.toString())
        }

        override fun modify(entry: PropertyEntry) {
            PatientHunterProperty.from(entry)?.let {
                val info = it.info.upgrade(getModifier())
                entry.setProperty(PatientHunterProperty.getKey(), PatientHunterProperty(it.getAbility(), info))
            }
        }
    }

    data class Info(val damageBonusSec: Int,
                    val damageBonusMax: Int){
        fun upgrade(modifier: Info): Info {
            val sec = damageBonusSec + modifier.damageBonusSec
            val max = damageBonusMax + modifier.damageBonusMax
            return Info(sec, max)
        }
    }
}