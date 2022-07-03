package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.ArcherSentientBowsProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ArcherSentientBowsTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ArcherSentientBowsProperty){
            return listOf(
                Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ARCHER_SENTIENT_BOWS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(effect.getArcherSentientBows().toString()).formatted(Formatting.WHITE)))
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is ArcherSentientBowsProperty){
                return listOf(
                    Symbol.ALTER_HITS.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_ARCHER_SENTIENT_BOWS.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText(signed(effect.getArcherSentientBows())).formatted(Formatting.WHITE)))
            }
            return emptyList()
        }
    }
}