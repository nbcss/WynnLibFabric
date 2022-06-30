package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object DamageTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is DamageProperty){
            val damage = effect.getDamage()
            if(!damage.isZero()){
                val tooltip: MutableList<Text> = ArrayList()
                val color = if (damage.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
                tooltip.add(Symbol.DAMAGE.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText("${damage.getTotalDamage()}%").formatted(color)))
                //add neutral damage
                if (damage.getNeutralDamage() != 0){
                    tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                        .append(Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD))
                        .append(": ${damage.getNeutralDamage()}%)"))
                }
                //add element damages
                Element.values().forEach {
                    val value = damage.getElementalDamage(it)
                    if (value != 0){
                        tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                            .append(it.formatted(Formatting.DARK_GRAY, "tooltip.damage"))
                            .append(": ${value}%)"))
                    }
                }
                return tooltip
            }
        }
        return emptyList()
    }
}