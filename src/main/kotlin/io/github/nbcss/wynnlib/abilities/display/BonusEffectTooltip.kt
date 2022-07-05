package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.BonusEffectProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object BonusEffectTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is BonusEffectProperty) {
            val bonus = effect.getEffectBonus()
            return listOf(Symbol.EFFECT.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_EFFECT.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${signed(bonus.getEffectModifier())}% ").formatted(Formatting.WHITE))
                .append(bonus.getEffectType().formatted(Formatting.GRAY)))
        }
        return emptyList()
    }
}