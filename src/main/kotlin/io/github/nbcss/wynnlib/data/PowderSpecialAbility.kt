package io.github.nbcss.wynnlib.data

import net.minecraft.text.Text

class PowderSpecialAbility(private val type: PowderSpecial) {
    //todo
    fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        val color = type.getElement().color
        tooltip.add(type.formatted(color))
        return tooltip
    }
}