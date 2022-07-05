package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.AreaOfEffectProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object AreaOfEffectTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is AreaOfEffectProperty) {
            val range = effect.getAreaOfEffect().getRange()
            if (range.isZero()){
                val tooltip: MutableList<Text> = ArrayList()
                effect.getAreaOfEffect().getShape()?.let {
                    tooltip.add(Symbol.AOE.asText().append(" ")
                        .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                        .append(it.formatted(Formatting.WHITE)))
                }
                return tooltip
            }
            val suffix = if(range.upper() <= 1)
                Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
            var value = removeDecimal(range.lower())
            if(!range.isConstant()){
                value = "$value-${removeDecimal(range.upper())}"
            }
            //val value = suffix.formatted(Formatting.WHITE, null, if (range % 1.0 != 0.0) range else range.toInt())
            val text = Symbol.AOE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                .append(suffix.formatted(Formatting.WHITE, null, value))
            effect.getAreaOfEffect().getShape()?.let {
                text.append(LiteralText(" (").formatted(Formatting.GRAY))
                    .append(it.formatted(Formatting.GRAY))
                    .append(LiteralText(")").formatted(Formatting.GRAY))
            }
            return listOf(text)
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is AreaOfEffectProperty) {
                val range = effect.getAreaOfEffect().getRange()
                if (range.isZero()){
                    val tooltip: MutableList<Text> = ArrayList()
                    effect.getAreaOfEffect().getShape()?.let {
                        tooltip.add(Symbol.AOE.asText().append(" ")
                            .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                            .append(it.formatted(Formatting.WHITE)))
                    }
                    return tooltip
                }
                val suffix = if(range.upper() <= 1)
                    Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
                var value = (if (range.lower() > 0) "+" else "") + removeDecimal(range.lower())
                val color = if (range.lower() < 0) Formatting.RED else Formatting.WHITE
                if(!range.isConstant()){
                    value = "$value-${removeDecimal(range.upper())}"
                }
                val text = Symbol.AOE.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                    .append(suffix.formatted(color, null, value))
                effect.getAreaOfEffect().getShape()?.let {
                    text.append(LiteralText(" (").formatted(Formatting.GRAY))
                        .append(it.translate().formatted(Formatting.GRAY))
                        .append(LiteralText(")").formatted(Formatting.GRAY))
                }
                return listOf(text)
            }
            return emptyList()
        }
    }
}