package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DurationProperty(ability: Ability,
                       private val duration: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return DurationProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "duration"
    }

    fun getDuration(): Double = duration

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), removeDecimal(duration))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val value = Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(duration))
        return listOf(Symbol.DURATION.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }

    class Modifier(ability: Ability, private val modifier: Double):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data.asDouble)
            }
            override fun getKey(): String = "duration_modifier"
        }

        fun getDurationModifier(): Double = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), removeDecimal(modifier))
        }

        override fun modify(entry: PropertyEntry) {
            entry.getProperty(DurationProperty.getKey())?.let {
                val duration = round((it as DurationProperty).getDuration() + getDurationModifier())
                entry.setProperty(DurationProperty.getKey(), DurationProperty(it.getAbility(), duration))
            }
        }

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