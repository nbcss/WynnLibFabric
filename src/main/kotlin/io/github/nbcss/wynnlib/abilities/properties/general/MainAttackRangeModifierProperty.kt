package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackRangeModifierProperty(ability: Ability, data: JsonElement):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<MainAttackRangeModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackRangeModifierProperty {
            return MainAttackRangeModifierProperty(ability, data)
        }
        override fun getKey(): String = "main_range_modifier"
    }
    private val modifier: Double = data.asDouble

    fun getMainAttackRangeModifier(): Double = modifier

    override fun modify(entry: PropertyEntry) {
        MainAttackRangeProperty.from(entry)?.let {
            val range = it.getMainAttackRange() + getMainAttackRangeModifier()
            entry.setProperty(MainAttackRangeProperty.getKey(), MainAttackRangeProperty(it.getAbility(), range))
        }
    }

    override fun getTooltip(): List<Text> {
        val value = (if(modifier <= 1) Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS)
            .formatted(Formatting.WHITE, null, (if (modifier > 0) "+" else "") + removeDecimal(modifier))
        return listOf(Symbol.RANGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_RANGE.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }
}