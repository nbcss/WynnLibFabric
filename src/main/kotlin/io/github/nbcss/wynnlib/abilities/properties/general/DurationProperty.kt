package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DurationProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return DurationProperty(ability, data)
        }
        override fun getKey(): String = "duration"
    }
    private val duration: Double = data.asDouble
    init {
        ability.putPlaceholder(getKey(), removeDecimal(duration))
    }

    fun getDuration(): Double = duration

    override fun getTooltip(): List<Text> {
        val value = Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(duration))
        return listOf(Symbol.DURATION.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }

    class Modifier(ability: Ability, data: JsonElement): AbilityProperty(ability) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "duration_modifier"
        }
        private val modifier: Double = data.asDouble
        init {
            ability.putPlaceholder(getKey(), removeDecimal(modifier))
        }

        fun getDurationModifier(): Double = modifier

        override fun getTooltip(): List<Text> {
            val color = if(modifier < 0) Formatting.RED else Formatting.GREEN
            val value = Translations.TOOLTIP_SUFFIX_S.formatted(color, null,
                (if(modifier > 0) "+" else "") + removeDecimal(modifier))
            return listOf(Symbol.DURATION.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
    }
}