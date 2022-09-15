package io.github.nbcss.wynnlib.events

import net.minecraft.item.ItemStack
import net.minecraft.text.Text

data class InventoryUpdateEvent(val title: Text,
                                val stacks: List<ItemStack>,
                                val cursor: ItemStack) {

    companion object: EventHandler.HandlerList<InventoryUpdateEvent>()
}