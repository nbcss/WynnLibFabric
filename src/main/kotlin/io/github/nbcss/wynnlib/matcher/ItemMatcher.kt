package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*
import java.util.function.Supplier

interface ItemMatcher {
    //fun toBaseItem(item: ItemStack): BaseItem?
    /**
     * Match item to rarity color provider.
     * If the item cannot match with an associated color provider, the method will return null.
     *
     * @param item the item stack
     * @param tooltip the tooltip of the itemstack (you cannot and should not modify it)
     * @return color supplier, or null if the given item does not have associated color in the matcher.
     */
    fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>?

    companion object {
        private val colorCacheMap: MutableMap<String, Supplier<Color?>> = WeakHashMap()
        private val nullSupplier: Supplier<Color?> = Supplier<Color?> {null}
        private val colorMatchers: List<ItemMatcher> = listOf(
            EquipmentMatcher,
            BoxMatcher,
            IngredientMatcher,
            MaterialMatcher,
        )
        /*fun matchesItem(item: ItemStack): BaseItem? {
            return null
        }*/

        fun toRarityColor(item: ItemStack): Color? {
            //println(item.name)
            if (item.isEmpty)
                return null
            val key = item.writeNbt(NbtCompound()).toString()
            val cache = colorCacheMap[key]
            if (cache != null) {
                return cache.get()
            }
            val tooltip = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
            val supplier = colorMatchers.firstNotNullOfOrNull { it.toRarityColor(item, tooltip) } ?: nullSupplier
            colorCacheMap[key] = supplier
            return supplier.get()
            //val colors = listOf(Color.DARK_PURPLE, Color.RED, Color.AQUA, Color.PINK, Color.YELLOW)
            //return colors.random()
        }
    }
}