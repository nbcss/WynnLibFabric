package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object EquipmentItemMatcher: ItemMatcher {
    override fun toItem(item: ItemStack, tooltip: List<Text>): BaseItem? {
        if (!item.hasCustomName())
            return null
        val name = item.name.asString()
        if (name.length > 2 && name.startsWith("§")){
            return RegularEquipmentRegistry.fromName(name.substring(2))
        }
        return null
    }
}