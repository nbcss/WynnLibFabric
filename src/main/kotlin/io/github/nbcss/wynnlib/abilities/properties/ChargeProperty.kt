package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ChargeProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ChargeProperty(ability, data)
        }
        override fun getKey(): String = "charges"
    }
    private val charges: Int = data.asInt

    fun getCharges(): Int = charges

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_CHARGES.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(charges.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, data: JsonElement): AbilityProperty(ability) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "charges_modifier"
        }
        private val modifier: Int = data.asInt

        fun getChargesModifier(): Int = modifier

        override fun getTooltip(): List<Text> {
            val color = if (modifier < 0) Formatting.RED else Formatting.WHITE
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_CHARGES.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(color)))
        }
    }
}