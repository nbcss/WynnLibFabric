package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TotalHealProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type<TotalHealProperty> {
        override fun create(ability: Ability, data: JsonElement): TotalHealProperty {
            return TotalHealProperty(ability, data)
        }
        override fun getKey(): String = "total_heal"
    }
    private val heal: Int = data.asInt

    fun getTotalHeal(): Int = heal

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_TOTAL_HEAL.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$heal%").formatted(Formatting.WHITE)))
    }
}