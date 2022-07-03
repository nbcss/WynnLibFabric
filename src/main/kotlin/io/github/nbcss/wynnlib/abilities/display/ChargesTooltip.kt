package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.ChargeProperty
import io.github.nbcss.wynnlib.abilities.properties.ManaCostProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ChargesTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ChargeProperty){
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_CHARGES.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(effect.getCharges().toString()).formatted(Formatting.WHITE)))
        }
        return emptyList()
    }
}