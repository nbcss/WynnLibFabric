package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackRangeProperty(ability: Ability,
                              private val range: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MainAttackRangeProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackRangeProperty {
            return MainAttackRangeProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "main_attack_range"
    }

    fun getMainAttackRange(): Double = range

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val value = (if(range <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
            .formatted(Formatting.WHITE, null, removeDecimal(range))
        return listOf(Symbol.RANGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_RANGE.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }
}