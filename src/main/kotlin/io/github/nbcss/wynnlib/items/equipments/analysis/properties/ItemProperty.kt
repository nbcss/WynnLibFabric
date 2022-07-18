package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

interface ItemProperty: Keyed {
    fun set(tooltip: List<Text>, line: Int): Int
}