package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.DurationProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object DurationTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is DurationProperty) {
            val duration = effect.getDuration()
            val value = Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null,
                if (duration % 1.0 != 0.0) duration else duration.toInt())
            return listOf(
                Symbol.DURATION.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
        return emptyList()
    }
}