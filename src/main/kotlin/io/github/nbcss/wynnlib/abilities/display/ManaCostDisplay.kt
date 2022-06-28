package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.properties.ManaCostProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ManaCostDisplay: EffectDisplay {
    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val cost = ManaCostProperty.getValue(provider).toString()
        return listOf(
            Symbol.MANA.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(cost).formatted(Formatting.WHITE)))
    }
}