package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.AreaOfEffectProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object AreaOfEffectTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is AreaOfEffectProperty) {
            val range = effect.getAreaOfEffect().getRange()
            val suffix = if(range.upper() <= 1)
                Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
            var value = (if (range.lower() % 1.0 != 0.0) range.lower() else range.lower().toInt()).toString()
            if(!range.isConstant()){
                value = "$value-${(if (range.upper() % 1.0 != 0.0) range.upper() else range.upper().toInt())}"
            }
            //val value = suffix.formatted(Formatting.WHITE, null, if (range % 1.0 != 0.0) range else range.toInt())
            val text = Symbol.AOE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                .append(suffix.formatted(Formatting.WHITE, null, value))
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