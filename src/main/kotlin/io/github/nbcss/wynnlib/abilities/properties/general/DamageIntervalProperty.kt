package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_INTERVAL_HITS
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.floor

class DamageIntervalProperty(ability: Ability,
                             private val interval: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<DamageIntervalProperty> {
        override fun create(ability: Ability, data: JsonElement): DamageIntervalProperty {
            return DamageIntervalProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "damage_interval"
    }

    fun getInterval(): Double = interval

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), removeDecimal(interval))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val text = Symbol.DAMAGE_INTERVAL.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DAMAGE_INTERVAL.formatted(Formatting.GRAY).append(": "))
            .append(Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(interval)))
        DurationProperty.from(provider)?.let {
            val hits = floor((it.getDuration() / getInterval())).toInt()
            text.append(LiteralText(" (").formatted(Formatting.DARK_GRAY)
                .append(TOOLTIP_ABILITY_INTERVAL_HITS.formatted(Formatting.DARK_GRAY, label = null, "$hits"))
                .append(")"))
        }
        return listOf(text)
    }

    class Modifier(ability: Ability,
                   private val modifier: Double):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asDouble)
            }
            override fun getKey(): String = "damage_interval_modifier"
        }

        fun getModifier(): Double = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), removeDecimal(modifier))
        }

        override fun modify(entry: PropertyEntry) {
            DamageIntervalProperty.from(entry)?.let {
                val value = round(it.getInterval() + getModifier())
                entry.setProperty(DamageIntervalProperty.getKey(), DamageIntervalProperty(it.getAbility(), value))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val color = if (modifier <= 0) Formatting.GREEN else Formatting.RED
            val value = Translations.TOOLTIP_SUFFIX_S.formatted(color, null,
                (if (modifier > 0) "+" else "") + removeDecimal(modifier))
            return listOf(Symbol.DAMAGE_INTERVAL.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_DAMAGE_INTERVAL.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
    }
}