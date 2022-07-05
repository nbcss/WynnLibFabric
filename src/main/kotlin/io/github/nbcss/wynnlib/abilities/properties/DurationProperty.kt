package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
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

    fun getDuration(): Double = duration

    override fun getTooltip(): List<Text> {
        val value = Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(duration))
        return listOf(Symbol.DURATION.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }
}