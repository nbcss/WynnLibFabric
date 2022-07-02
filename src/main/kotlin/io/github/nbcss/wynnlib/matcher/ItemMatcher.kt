package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
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
            val tooltip = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
            //todo
            tooltip.forEach {
                val s = it.string
                //println(s)
                for (tier in Tier.values()) {
                    if (s.contains(tier.displayName)){
                        return Settings.getTierColor(tier)
                    }
                }
            }
            //val colors = listOf(Color.DARK_PURPLE, Color.RED, Color.AQUA, Color.PINK, Color.YELLOW)
            //return colors.random()
            return null
        }
    }
}