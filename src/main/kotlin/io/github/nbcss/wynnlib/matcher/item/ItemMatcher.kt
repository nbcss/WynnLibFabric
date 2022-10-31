package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.matcher.MatchableItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*

interface ItemMatcher {
    fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): MatchableItem?

    companion object {
        private val itemCacheMap: MutableMap<String, MatchableItem?> = WeakHashMap()
        private val itemMatchers: List<ItemMatcher> = listOf(
            EquipmentItemMatcher,
            BoxMatcher,
            IngredientMatcher,
            MaterialMatcher,
            PowderMatcher,
            EmeraldMatcher
        )

        fun toItem(item: ItemStack): MatchableItem? {
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
            var inMarket = false
            if (name.endsWith("\u00C0")) {
                name = name.substring(0, name.length - 1)
                inMarket = true
            }
            val result = itemMatchers.firstNotNullOfOrNull { it.toItem(item, name, tooltip, inMarket) }
            itemCacheMap[key] = result
            return result
        }
    }

    data class MatchResult(val item: BaseItem, val marketPrice: Int?)
}