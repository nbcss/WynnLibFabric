package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackDamageModifierProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return MainAttackDamageModifierProperty(ability, data)
        }
        override fun getKey(): String = "main_damage_modifier"
    }
    private val modifier: Int = data.asInt

    fun getMainAttackDamageModifier(): Int = modifier

    fun getMainAttackDamageModifierRate(): Double = getMainAttackDamageModifier() / 100.0

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${signed(modifier)}%").formatted(Formatting.WHITE)))
    }
}