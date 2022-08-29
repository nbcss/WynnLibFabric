package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class RangeProperty(ability: Ability,
                         private val range: Double,
                         private val variant: Boolean = false):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<RangeProperty> {
        override fun create(ability: Ability, data: JsonElement): RangeProperty {
            val string = data.asString
            if (string.startsWith("~")){
                return RangeProperty(ability, string.substring(1).toDouble(), true)
            }
            return RangeProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "range"
    }

    fun getRange(): Double = range

    fun modify(modifier: Double): RangeProperty {
        return RangeProperty(getAbility(), round(range + modifier), variant)
    }

    override fun getOverviewTip(): Text {
        val text = Symbol.RANGE.asText().append(" ")
        if (variant)
            text.append("±")
        return text.append(LiteralText(removeDecimal(getRange())).formatted(Formatting.WHITE))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val value = (if(range <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
            .formatted(Formatting.WHITE, null, removeDecimal(range))
        return listOf(Symbol.RANGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(if (variant) "±" else "").formatted(Formatting.WHITE)).append(value))
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
                entry.setProperty(RangeProperty.getKey(), it.modify(modifier))
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