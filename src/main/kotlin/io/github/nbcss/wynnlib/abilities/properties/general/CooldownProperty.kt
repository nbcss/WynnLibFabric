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

class CooldownProperty(ability: Ability, private val cooldown: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return CooldownProperty(ability, data.asDouble)
        }
        override fun getKey(): String = "cooldown"
    }

    fun getCooldown(): Double = cooldown

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), removeDecimal(cooldown))
    }

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.COOLDOWN.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_COOLDOWN.formatted(Formatting.GRAY).append(": "))
            .append(Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(cooldown))))
    }

    class Modifier(ability: Ability, private val modifier: Double):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data.asDouble)
            }
            override fun getKey(): String = "cooldown_modifier"
        }

        fun getCooldownModifier(): Double = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), removeDecimal(modifier))
        }

        override fun modify(entry: PropertyEntry) {
            entry.getProperty(CooldownProperty.getKey())?.let {
                val cd = round((it as CooldownProperty).getCooldown() + getCooldownModifier())
                entry.setProperty(CooldownProperty.getKey(), CooldownProperty(it.getAbility(), cd))
            }
        }

        override fun getTooltip(): List<Text> {
            val color = if (modifier < 0) Formatting.GREEN else Formatting.RED
            val value = Translations.TOOLTIP_SUFFIX_S.formatted(color, null,
                (if (modifier > 0) "+" else "") + removeDecimal(modifier))
            return listOf(Symbol.COOLDOWN.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_COOLDOWN.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
    }
}