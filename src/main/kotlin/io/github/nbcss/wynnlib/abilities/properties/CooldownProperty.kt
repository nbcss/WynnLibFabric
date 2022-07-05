package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class CooldownProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return CooldownProperty(ability, data)
        }
        override fun getKey(): String = "cooldown"
    }
    private val cooldown: Double = data.asDouble
    init {
        ability.putPlaceholder(CooldownModifierProperty.getKey(), removeDecimal(cooldown))
    }

    fun getCooldown(): Double = cooldown

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.COOLDOWN.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_COOLDOWN.formatted(Formatting.GRAY).append(": "))
            .append(Translations.TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, removeDecimal(cooldown))))
    }
}