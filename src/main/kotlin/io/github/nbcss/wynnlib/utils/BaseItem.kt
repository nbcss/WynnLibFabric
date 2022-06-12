package io.github.nbcss.wynnlib.utils

import net.minecraft.item.ItemStack

interface BaseItem {
    fun getDisplayName(): String
    fun getIcon(): ItemStack
    fun getColor(): Int
}