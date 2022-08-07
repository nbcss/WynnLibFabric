package io.github.nbcss.wynnlib.abilities.properties.assassin

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
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

class MaxMarkedProperty(ability: Ability,
                        private val maxMarked: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MaxMarkedProperty> {
        override fun create(ability: Ability, data: JsonElement): MaxMarkedProperty {
            return MaxMarkedProperty(ability, data.asInt)
        }
        override fun getKey(): String = "max_marked"
    }

    fun getMaxMarked(): Int = maxMarked

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), getMaxMarked().toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val marked = Translations.TOOLTIP_ABILITY_ASSASSIN_MARKED.translate().string
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${marked}): "))
            .append(LiteralText(getMaxMarked().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "max_marked_modifier"
        }

        fun getModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(MaxMarkedProperty.getKey(), getModifier().toString())
        }

        override fun modify(entry: PropertyEntry) {
            MaxMarkedProperty.from(entry)?.let {
                val marked = it.getMaxMarked() + getModifier()
                entry.setProperty(MaxMarkedProperty.getKey(), MaxMarkedProperty(it.getAbility(), marked))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val value = Translations.TOOLTIP_ABILITY_ASSASSIN_MARKED.translate().string
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${value}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}