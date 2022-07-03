package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.CooldownProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object CooldownTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is CooldownProperty) {
            val cd = effect.getCooldown()
            return listOf(Symbol.COOLDOWN.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_COOLDOWN.formatted(Formatting.GRAY).append(": "))
                .append(Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(cd))))
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is CooldownProperty) {
                val cd = effect.getCooldown()
                val color = if (cd < 0) Formatting.WHITE else Formatting.RED
                val value = Translations.TOOLTIP_SUFFIX_S.formatted(color, null,
                    (if (cd > 0) "+" else "") + removeDecimal(cd))
                return listOf(Symbol.COOLDOWN.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_COOLDOWN.formatted(Formatting.GRAY).append(": "))
                    .append(value))
            }
            return emptyList()
        }
    }
}