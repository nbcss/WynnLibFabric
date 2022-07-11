package io.github.nbcss.wynnlib.abilities.properties.mage

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

class MaxWindedProperty(ability: Ability,
                        private val maxWinded: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MaxWindedProperty> {
        override fun create(ability: Ability, data: JsonElement): MaxWindedProperty {
            return MaxWindedProperty(ability, data.asInt)
        }
        override fun getKey(): String = "max_winded"
    }

    fun getMaxWinded(): Int = maxWinded

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), getMaxWinded().toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val max = Translations.TOOLTIP_ABILITY_MAGE_WINDED.translate().string
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${max}): "))
            .append(LiteralText(getMaxWinded().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "max_winded_modifier"
        }

        fun getWindedModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(MaxWindedProperty.getKey(), getWindedModifier().toString())
        }

        override fun modify(entry: PropertyEntry) {
            MaxWindedProperty.from(entry)?.let {
                val focus = it.getMaxWinded() + getWindedModifier()
                entry.setProperty(MaxWindedProperty.getKey(), MaxWindedProperty(it.getAbility(), focus))
            }
        }

        override fun getTooltip(): List<Text> {
            val max = Translations.TOOLTIP_ABILITY_MAGE_WINDED.translate().string
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${max}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}