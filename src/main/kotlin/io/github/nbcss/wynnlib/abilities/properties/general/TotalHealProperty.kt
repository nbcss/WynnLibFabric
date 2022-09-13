package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_TOTAL_HEAL_SUFFIX
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TotalHealProperty(ability: Ability,
                        private val heal: Double):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<TotalHealProperty> {
        override fun create(ability: Ability, data: JsonElement): TotalHealProperty {
            return TotalHealProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "total_heal"
    }

    fun getTotalHeal(): Double = heal

    override fun getOverviewTip(): Text {
        return Symbol.HEART.asText().append(" ").append(
            LiteralText("${removeDecimal(heal)}%").formatted(Formatting.WHITE)
        )
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val suffix = LiteralText(" (").formatted(Formatting.DARK_GRAY)
            .append(TOOLTIP_ABILITY_TOTAL_HEAL_SUFFIX.formatted(Formatting.DARK_GRAY)).append(")")
        return listOf(Symbol.HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_TOTAL_HEAL.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${removeDecimal(heal)}%").formatted(Formatting.WHITE)).append(suffix))
    }
}