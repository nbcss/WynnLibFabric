package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.ElementBoosterProperty
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ElementBoosterTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ElementBoosterProperty) {
            val booster = effect.getElementBooster()
            val element = booster.getElement()
            val tooltip: MutableList<Text> = ArrayList()
            if(!booster.getRawBooster().isZero()){
                var value = "+${booster.getRawBooster().lower()}"
                if(!booster.getRawBooster().isConstant()){
                    value = "$value-${booster.getRawBooster().upper()}"
                }
                tooltip.add(element.formatted(Formatting.GRAY, "tooltip.damage").append(": ")
                    .append(LiteralText(value).formatted(Formatting.WHITE)))
            }
            if(booster.getPctBooster() != 0){
                tooltip.add(element.formatted(Formatting.GRAY, "tooltip.damage").append(": ")
                    .append(LiteralText("+${booster.getPctBooster()}%").formatted(Formatting.WHITE)))
            }
            return tooltip
        }
        return emptyList()
    }
}