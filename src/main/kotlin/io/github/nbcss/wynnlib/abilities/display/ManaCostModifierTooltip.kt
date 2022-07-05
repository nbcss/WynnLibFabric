package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.ManaCostModifierProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ManaCostModifierTooltip: EffectTooltip {
    override fun get(effect: AbilityEffect): List<Text> {
        if (effect is ManaCostModifierProperty){
            val formatting = if (effect.getManaModifier() > 0) Formatting.RED else Formatting.WHITE
            val text = Symbol.MANA.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(effect.getManaModifier())).formatted(formatting))
            if (effect is BoundSpellProperty) {
                val tree = AbilityRegistry.fromCharacter(effect.getAbility().getCharacter())
                tree.getSpellAbility(effect.getSpell())?.let {
                    text.append(LiteralText(" (").formatted(Formatting.GRAY)
                        .append(it.formatted(Formatting.GRAY)).append(")"))
                }
            }
            return listOf(text)
        }
        return emptyList()
    }
}