package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object EquipmentItemMatcher: ItemMatcher {
    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>): RegularEquipment? {
        if (name.length > 2 && name.startsWith("§")){
            RegularEquipmentRegistry.fromName(name.substring(2))?.let { eq ->
                if (tooltip.asSequence()
                        .filter { it.siblings.isNotEmpty() }
                        .map { it.siblings[0] }
                        .any { it.asString().contains(eq.getTier().displayName) }) return eq
            }
        }
        return null
    }
}