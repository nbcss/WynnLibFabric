package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*
import java.util.regex.Pattern

interface ItemMatcher {
    fun toItem(item: ItemStack, name: String, tooltip: List<Text>): BaseItem?

    companion object {
        private val itemCacheMap: MutableMap<String, BaseItem?> = WeakHashMap()
        private val itemMatchers: List<ItemMatcher> = listOf(
            EquipmentItemMatcher,
        )

        fun toItem(item: ItemStack): BaseItem? {
            if (item.isEmpty)
                return null
            if (!item.hasCustomName())
                return null
            val key = item.writeNbt(NbtCompound()).toString()
            if (itemCacheMap.containsKey(key)) {
                return itemCacheMap[key]
            }
            val tooltip = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
            //market item
            var name = item.name.asString()
            if (name.endsWith("\u00C0")) {
                name = name.substring(0, name.length - 1)
            }
            val result = itemMatchers.firstNotNullOfOrNull { it.toItem(item, name, tooltip) }
            itemCacheMap[key] = result
            return result
        }
    }

    data class MatchResult(val item: BaseItem, val marketPrice: Int?)
}