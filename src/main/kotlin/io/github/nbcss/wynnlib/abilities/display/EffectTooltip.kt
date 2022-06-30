package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import net.minecraft.text.Text

interface EffectTooltip {
    fun get(effect: AbilityEffect): List<Text>
}