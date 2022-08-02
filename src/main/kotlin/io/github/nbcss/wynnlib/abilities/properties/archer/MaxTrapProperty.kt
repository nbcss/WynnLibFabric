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

class MaxTrapProperty(ability: Ability,
                      private val maxTrap: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MaxTrapProperty> {
        override fun create(ability: Ability, data: JsonElement): MaxTrapProperty {
            return MaxTrapProperty(ability, data.asInt)
        }
        override fun getKey(): String = "max_trap"
    }

    fun getMaxTraps(): Int = maxTrap

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), getMaxTraps().toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val trap = Translations.TOOLTIP_ABILITY_ARCHER_TRAPS.translate().string
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${trap}): "))
            .append(LiteralText(getMaxTraps().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "max_trap_modifier"
        }

        fun getTrapModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), getTrapModifier().toString())
        }

        override fun modify(entry: PropertyEntry) {
            MaxTrapProperty.from(entry)?.let {
                val traps = it.getMaxTraps() + getTrapModifier()
                entry.setProperty(MaxTrapProperty.getKey(), MaxTrapProperty(it.getAbility(), traps))
            }
        }

        override fun getTooltip(): List<Text> {
            val trap = Translations.TOOLTIP_ABILITY_ARCHER_TRAPS.translate().string
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${trap}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}