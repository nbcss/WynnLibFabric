package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MaxEffigyProperty(ability: Ability,
                        private val maxLimit: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<MaxEffigyProperty> {
        override fun create(ability: Ability, data: JsonElement): MaxEffigyProperty {
            return MaxEffigyProperty(ability, data.asInt)
        }
        override fun getKey(): String = "max_effigy"
    }

    fun getMaxLimit(): Int = maxLimit

    override fun getOverviewTip(): Text {
        return Symbol.CHARGE.asText().append(" ").append(
            LiteralText("$maxLimit").formatted(Formatting.WHITE)
        )
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), getMaxLimit().toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val name = Translations.TOOLTIP_ABILITY_SHAMAN_EFFIGY.translate().string
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
            .append(LiteralText(getMaxLimit().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "max_effigy_modifier"
        }

        fun getModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), getModifier().toString())
        }

        override fun modify(entry: PropertyEntry) {
            MaxEffigyProperty.from(entry)?.let {
                val limit = it.getMaxLimit() + getModifier()
                entry.setProperty(MaxEffigyProperty.getKey(), MaxEffigyProperty(it.getAbility(), limit))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val name = Translations.TOOLTIP_ABILITY_SHAMAN_EFFIGY.translate().string
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}