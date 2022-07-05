package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class DamageBonusProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return DamageBonusProperty(ability, data)
        }
        override fun getKey(): String = "damage_bonus"
    }
    private val bonus: Int = data.asInt

    fun getDamageBonus(): Int = bonus

    protected open fun getDamageBonusLabel(): Text? = null

    fun getDamageBonusRate(): Double = getDamageBonus() / 100.0

    override fun getTooltip(): List<Text> {
        val color = if (bonus < 0) Formatting.RED else Formatting.WHITE
        val value = LiteralText("${signed(bonus)}%").formatted(color)
        getDamageBonusLabel()?.let {
            value.append(it)
        }
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DAMAGE_BONUS.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }

    class Raw(ability: Ability, data: JsonElement): DamageBonusProperty(ability, data) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Raw(ability, data)
            }
            override fun getKey(): String = "raw_damage_bonus"
        }

        override fun getDamageBonusLabel(): Text? {
            return LiteralText(" (").formatted(Formatting.GRAY)
                .append(Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_RAW.formatted(Formatting.GRAY))
                .append(LiteralText(")").formatted(Formatting.GRAY))
        }
    }

    class PerFocus(ability: Ability, data: JsonElement): DamageBonusProperty(ability, data) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return PerFocus(ability, data)
            }
            override fun getKey(): String = "focus_damage_bonus"
        }

        override fun getDamageBonusLabel(): Text? {
            return LiteralText(" (").formatted(Formatting.DARK_GRAY)
                .append(Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_FOCUS.formatted(Formatting.DARK_GRAY))
                .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
        }
    }
}