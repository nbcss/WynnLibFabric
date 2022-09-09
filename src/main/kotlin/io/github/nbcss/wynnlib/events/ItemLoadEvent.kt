package io.github.nbcss.wynnlib.events

import net.minecraft.item.ItemStack

class ItemLoadEvent(val item: ItemStack) {
    companion object: EventHandler.HandlerList<ItemLoadEvent>()
}