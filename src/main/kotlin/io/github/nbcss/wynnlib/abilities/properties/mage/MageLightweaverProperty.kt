package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty

class MageLightweaverProperty(ability: Ability,
                              private val orbs: LightweaverOrb):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MageLightweaverProperty> {
        private const val HEAL_KEY = "heal"
        private const val TIME_KEY = "time_limit"
        private const val DURATION = "duration"
        private const val MAX_KEY = "max"
        override fun create(ability: Ability, data: JsonElement): MageLightweaverProperty {
            val json = data.asJsonObject
            val heal = if (json.has(HEAL_KEY)) json[HEAL_KEY].asDouble else 0.0
            val time = if (json.has(TIME_KEY)) json[TIME_KEY].asDouble else 0.0
            val duration = if (json.has(DURATION)) json[DURATION].asDouble else 0.0
            val maxOrbs = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
            return MageLightweaverProperty(ability, LightweaverOrb(heal, time, duration, maxOrbs))
        }
        override fun getKey(): String = "lightweaver"
    }

    fun getOrbs(): LightweaverOrb = orbs

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("lightweaver.heal", orbs.heal.toString())
        container.putPlaceholder("lightweaver.time_limit", orbs.time.toString())
        container.putPlaceholder("lightweaver.duration", orbs.duration.toString())
        container.putPlaceholder("lightweaver.max", orbs.maxOrbs.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    class Modifier(ability: Ability,
                   private val modifier: LightweaverOrb):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                val json = data.asJsonObject
                val heal = if (json.has(HEAL_KEY)) json[HEAL_KEY].asDouble else 0.0
                val time = if (json.has(TIME_KEY)) json[TIME_KEY].asDouble else 0.0
                val duration = if (json.has(DURATION)) json[DURATION].asDouble else 0.0
                val maxOrbs = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
                return Modifier(ability, LightweaverOrb(heal, time, duration, maxOrbs))
            }
            override fun getKey(): String = "lightweaver_modifier"
        }

        fun getModifier(): LightweaverOrb = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder("lightweaver_modifier.heal", modifier.heal.toString())
            container.putPlaceholder("lightweaver_modifier.time_limit", modifier.time.toString())
            container.putPlaceholder("lightweaver_modifier.duration", modifier.duration.toString())
            container.putPlaceholder("lightweaver_modifier.max", modifier.maxOrbs.toString())
        }

        override fun modify(entry: PropertyEntry) {
            MageLightweaverProperty.from(entry)?.let {
                val upgrade = it.getOrbs().upgrade(getModifier())
                entry.setProperty(MageLightweaverProperty.getKey(), MageLightweaverProperty(it.getAbility(), upgrade))
            }
        }
    }

    data class LightweaverOrb(val heal: Double,
                              val time: Double,
                              val duration: Double,
                              val maxOrbs: Int){
        fun upgrade(modifier: LightweaverOrb): LightweaverOrb {
            val heal = this.heal + modifier.heal
            val time = this.time + modifier.time
            val duration = this.duration + modifier.duration
            val maxOrbs = this.maxOrbs + modifier.maxOrbs
            return LightweaverOrb(heal, time, duration, maxOrbs)
        }
    }
}