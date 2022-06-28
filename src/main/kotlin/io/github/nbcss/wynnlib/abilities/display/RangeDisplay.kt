package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object RangeDisplay: EffectDisplay {
    override fun getTooltip(provider: PropertyProvider): List<Text> {
        RangeProperty.read(provider)?.let {
            val value = (if(it <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
                .formatted(Formatting.WHITE, null, if (it % 1.0 != 0.0) it else it.toInt())
            return listOf(Symbol.RANGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_RANGE.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
        return emptyList()
    }
}