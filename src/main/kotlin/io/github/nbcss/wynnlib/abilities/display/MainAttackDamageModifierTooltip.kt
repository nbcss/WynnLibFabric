package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.MainAttackDamageModifierProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object MainAttackDamageModifierTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is MainAttackDamageModifierProperty){
            val damage = effect.getMainAttackDamageModifier()
            return listOf(Symbol.DAMAGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${signed(damage)}%").formatted(Formatting.WHITE)))
        }
        return emptyList()
    }
}