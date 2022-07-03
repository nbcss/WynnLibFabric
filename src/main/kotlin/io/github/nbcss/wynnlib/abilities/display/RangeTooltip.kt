package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object RangeTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is RangeProperty) {
            val range = effect.getRange()
            val value = (if(range <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
                .formatted(Formatting.WHITE, null, removeDecimal(range))
            return listOf(Symbol.RANGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is RangeProperty) {
                val range = effect.getRange()
                val value = (if(range <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
                    .formatted(Formatting.WHITE, null, (if (range > 0) "+" else "") + removeDecimal(range))
                return listOf(
                    Symbol.RANGE.asText().append(" ")
                        .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
                        .append(value))
            }
            return emptyList()
        }
    }
}