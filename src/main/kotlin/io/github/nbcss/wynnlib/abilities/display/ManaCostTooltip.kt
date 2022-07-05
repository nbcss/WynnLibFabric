package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.ManaCostProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ManaCostTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ManaCostProperty){
            return listOf(Symbol.MANA.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(effect.getManaCost().toString()).formatted(Formatting.WHITE)))
        }
        return emptyList()
    }
}