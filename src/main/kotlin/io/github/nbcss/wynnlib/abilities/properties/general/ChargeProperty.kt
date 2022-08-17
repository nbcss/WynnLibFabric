package io.github.nbcss.wynnlib.abilities.properties.general

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
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ChargeProperty(ability: Ability,
                     private val charges: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<ChargeProperty> {
        override fun create(ability: Ability, data: JsonElement): ChargeProperty {
            return ChargeProperty(ability, data.asInt)
        }
        override fun getKey(): String = "charges"
    }

    fun getCharges(): Int = charges

    override fun getOverviewTip(): Text? {
        return Symbol.CHARGE.asText().append(" ")
            .append(LiteralText(charges.toString()).formatted(Formatting.WHITE))
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), charges.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_CHARGES.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(charges.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "charges_modifier"
        }

        fun getChargesModifier(): Int = modifier

        override fun modify(entry: PropertyEntry) {
            ChargeProperty.from(entry)?.let {
                val charges = it.getCharges() + getChargesModifier()
                entry.setProperty(ChargeProperty.getKey(), ChargeProperty(it.getAbility(), charges))
            }
        }

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val color = if (modifier < 0) Formatting.RED else Formatting.GREEN
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_CHARGES.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(color)))
        }
    }
}