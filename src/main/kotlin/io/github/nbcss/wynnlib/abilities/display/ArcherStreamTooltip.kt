package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.ArcherStreamProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ArcherStreamTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ArcherStreamProperty){
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ARCHER_STREAM.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(effect.getArcherStreams().toString()).formatted(Formatting.WHITE)))
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is ArcherStreamProperty){
                return listOf(Symbol.ALTER_HITS.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_ARCHER_STREAM.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText(signed(effect.getArcherStreams())).formatted(Formatting.WHITE)))
            }
            return emptyList()
        }
    }
}