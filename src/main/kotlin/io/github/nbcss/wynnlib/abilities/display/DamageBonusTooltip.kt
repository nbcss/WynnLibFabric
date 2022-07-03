package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.DamageBonusProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object DamageBonusTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is DamageBonusProperty) {
            val color = if (effect.getDamageBonus() < 0) Formatting.RED else Formatting.WHITE
            val value = LiteralText("${signed(effect.getDamageBonus())}%").formatted(color)
            effect.getDamageBonusLabel()?.let {
                value.append(it)
            }
            return listOf(Symbol.DAMAGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_DAMAGE_BONUS.formatted(Formatting.GRAY).append(": "))
                .append(value))
        }
        return emptyList()
    }
}