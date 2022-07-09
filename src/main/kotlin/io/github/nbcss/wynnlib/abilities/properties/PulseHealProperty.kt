package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class PulseHealProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return PulseHealProperty(ability, data)
        }
        override fun getKey(): String = "pulse_heal"
    }
    private val heal: Int = data.asInt

    fun getPulseHeal(): Int = heal

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_PULSE_HEAL.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$heal%").formatted(Formatting.WHITE)))
    }
}