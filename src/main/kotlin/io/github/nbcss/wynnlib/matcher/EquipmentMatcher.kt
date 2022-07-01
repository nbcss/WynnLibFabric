package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack

object EquipmentMatcher: ItemMatcher {
    override fun toBaseItem(item: ItemStack): BaseItem? {
        return null
    }

    override fun toRarityColor(item: ItemStack): Color? {

        return null
    }
}