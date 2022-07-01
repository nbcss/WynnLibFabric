package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack

interface ItemMatcher {
    fun toBaseItem(item: ItemStack): BaseItem?
    fun toRarityColor(item: ItemStack): Color?

    companion object {
        fun matchesItem(item: ItemStack): BaseItem? {
            //todo
            return null
        }

        fun toRarityColor(item: ItemStack): Color? {
            //println(item.name)
            if (item.isEmpty)
                return null
            //todo
            //val colors = listOf(Color.DARK_PURPLE, Color.RED, Color.AQUA, Color.PINK, Color.YELLOW)
            //return colors.random()
            return Color.AQUA
        }
    }
}