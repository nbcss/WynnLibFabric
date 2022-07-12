package io.github.nbcss.wynnlib.abilities.properties.archer

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MaxFocusProperty(ability: Ability,
                       private val maxFocus: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MaxFocusProperty> {
        override fun create(ability: Ability, data: JsonElement): MaxFocusProperty {
            return MaxFocusProperty(ability, data.asInt)
        }
        override fun getKey(): String = "max_focus"
    }

    fun getMaxFocus(): Int = maxFocus

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), getMaxFocus().toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val focus = Translations.TOOLTIP_ABILITY_ARCHER_FOCUS.translate().string
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${focus}): "))
            .append(LiteralText(getMaxFocus().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "max_focus_modifier"
        }

        fun getFocusModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(MaxFocusProperty.getKey(), getFocusModifier().toString())
        }

        override fun modify(entry: PropertyEntry) {
            MaxFocusProperty.from(entry)?.let {
                val focus = it.getMaxFocus() + getFocusModifier()
                entry.setProperty(MaxFocusProperty.getKey(), MaxFocusProperty(it.getAbility(), focus))
            }
        }

        override fun getTooltip(): List<Text> {
            val focus = Translations.TOOLTIP_ABILITY_ARCHER_FOCUS.translate().string
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${focus}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}