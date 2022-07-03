package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_HITS
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object DamageTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is DamageProperty){
            val damage = effect.getDamage()
            val tooltip: MutableList<Text> = ArrayList()
            if(!damage.isZero()){
                val color = if (damage.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
                val total = Symbol.DAMAGE.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText("${damage.getTotalDamage()}%").formatted(color))
                damage.getDamageLabel()?.let {
                    total.append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
                        .append(it.formatted(Formatting.DARK_GRAY))
                        .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
                }
                tooltip.add(total)
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
            }
            if (damage.getHits() != 1){
                tooltip.add(Symbol.HITS.asText().append(" ")
                    .append(TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText("${damage.getHits()}").formatted(Formatting.WHITE)))
            }
            return tooltip
        }
        return emptyList()
    }

    object Modifier: EffectTooltip {
        override fun get(effect: AbilityEffect): List<Text> {
            if (effect is DamageProperty){
                val damage = effect.getDamage()
                val tooltip: MutableList<Text> = ArrayList()
                if(!damage.isZero()){
                    val color = if (damage.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
                    val total = Symbol.DAMAGE.asText().append(" ")
                        .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                        .append(LiteralText("${signed(damage.getTotalDamage())}%").formatted(color))
                    damage.getDamageLabel()?.let {
                        total.append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
                            .append(it.formatted(Formatting.DARK_GRAY))
                            .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
                    }
                    tooltip.add(total)
                    //add neutral damage
                    if (damage.getNeutralDamage() != 0){
                        tooltip.add(
                            LiteralText("   (").formatted(Formatting.DARK_GRAY)
                                .append(Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD))
                                .append(": ${signed(damage.getNeutralDamage())}%)"))
                    }
                    //add element damages
                    Element.values().forEach {
                        val value = damage.getElementalDamage(it)
                        if (value != 0){
                            tooltip.add(
                                LiteralText("   (").formatted(Formatting.DARK_GRAY)
                                    .append(it.formatted(Formatting.DARK_GRAY, "tooltip.damage"))
                                    .append(": ${signed(value)}%)"))
                        }
                    }
                }
                if (damage.getHits() != 0){
                    val hitsColor = if (damage.getHits() > 0) Formatting.WHITE else Formatting.RED
                    tooltip.add(Symbol.HITS.asText().append(" ")
                        .append(TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                        .append(LiteralText(signed(damage.getHits())).formatted(hitsColor)))
                }
                return tooltip
            }
            return emptyList()
        }
    }
}