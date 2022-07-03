package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.ReplacingProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ReplacingTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ReplacingProperty && effect.getReplacingAbility() != null){
            return listOf(Symbol.ADD.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_REPLACING.formatted(Formatting.GRAY).append(": "))
                .append(effect.getReplacingAbility()!!.formatted(Formatting.WHITE)))
        }
        return emptyList()
    }
}