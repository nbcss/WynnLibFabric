package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

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
        container.putPlaceholder("lightweaver.heal", removeDecimal(orbs.heal))
        container.putPlaceholder("lightweaver.time_limit", removeDecimal(orbs.time))
        container.putPlaceholder("lightweaver.duration", removeDecimal(orbs.duration))
        container.putPlaceholder("lightweaver.max", orbs.maxOrbs.toString())
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val name = Translations.TOOLTIP_ABILITY_MAGE_LIGHTWEAVER_ORBS.translate().string
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
            .append(LiteralText("${getOrbs().maxOrbs}").formatted(Formatting.WHITE)))
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
            container.putPlaceholder("lightweaver_modifier.heal", removeDecimal(modifier.heal))
            container.putPlaceholder("lightweaver_modifier.time_limit", removeDecimal(modifier.time))
            container.putPlaceholder("lightweaver_modifier.duration", removeDecimal(modifier.duration))
            container.putPlaceholder("lightweaver_modifier.max", modifier.maxOrbs.toString())
        }

        override fun modify(entry: PropertyEntry) {
            MageLightweaverProperty.from(entry)?.let {
                val upgrade = it.getOrbs().upgrade(getModifier())
                entry.setProperty(MageLightweaverProperty.getKey(), MageLightweaverProperty(it.getAbility(), upgrade))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val name = Translations.TOOLTIP_ABILITY_MAGE_LIGHTWEAVER_ORBS.translate().string
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
                .append(LiteralText(signed(modifier.maxOrbs)).formatted(colorOf(modifier.maxOrbs))))
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