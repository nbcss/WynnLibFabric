package io.github.nbcss.wynnlib.abilities.display

import io.github.nbcss.wynnlib.abilities.PropertyProvider
import net.minecraft.text.Text

interface EffectDisplay {
    fun getTooltip(provider: PropertyProvider): List<Text>
}