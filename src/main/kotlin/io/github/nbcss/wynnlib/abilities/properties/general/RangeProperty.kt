package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class RangeProperty(ability: Ability, private val range: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<RangeProperty> {
        override fun create(ability: Ability, data: JsonElement): RangeProperty {
            return RangeProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "range"
    }

    fun getRange(): Double = range

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val value = (if(range <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
            .formatted(Formatting.WHITE, null, removeDecimal(range))
        return listOf(Symbol.RANGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }

    class Modifier(ability: Ability,
                   private val modifier: Double):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asDouble)
            }
            override fun getKey(): String = "range_modifier"
        }

        fun getRangeModifier(): Double = modifier

        override fun modify(entry: PropertyEntry) {
            RangeProperty.from(entry)?.let {
                val range = round(it.getRange() + modifier)
                entry.setProperty(RangeProperty.getKey(), RangeProperty(it.getAbility(), range))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val color = if (modifier <= 0) Formatting.RED else Formatting.GREEN
            val value = (if(modifier <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
                .formatted(color, null, (if (modifier > 0) "+" else "") + removeDecimal(modifier))
            return listOf(Symbol.RANGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
    }

    class Clear(ability: Ability):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Clear> {
            override fun create(ability: Ability, data: JsonElement): Clear {
                return Clear(ability)
            }

            override fun getKey(): String = "range_clear"
        }

        override fun modify(entry: PropertyEntry) {
            RangeProperty.from(entry)?.let {
                entry.clearProperty(RangeProperty.getKey())
            }
        }
    }
}