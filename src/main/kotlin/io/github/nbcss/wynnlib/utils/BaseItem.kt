package io.github.nbcss.wynnlib.utils

import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface BaseItem {
    fun getDisplayName(): String
    fun getIcon(): ItemStack
    fun getColor(): Int
    fun getTooltip(): List<Text>
}