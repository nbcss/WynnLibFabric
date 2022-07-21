package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.matcher.color.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*

interface ItemMatcher {
    fun toItem(item: ItemStack, tooltip: List<Text>): BaseItem?

    companion object {
        private val itemCacheMap: MutableMap<String, BaseItem?> = WeakHashMap()
        private val itemMatchers: List<ItemMatcher> = listOf(
            EquipmentItemMatcher
        )

        fun toItem(item: ItemStack): BaseItem? {
            if (item.isEmpty)
                return null
            val key = item.writeNbt(NbtCompound()).toString()
            if (itemCacheMap.containsKey(key)) {
                return itemCacheMap[key]
            }
            val tooltip = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
            val result = itemMatchers.firstNotNullOfOrNull { it.toItem(item, tooltip) }
            itemCacheMap[key] = result
            return result
        }
    }
}