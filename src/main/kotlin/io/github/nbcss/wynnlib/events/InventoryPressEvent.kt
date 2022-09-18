package io.github.nbcss.wynnlib.events

import net.minecraft.client.gui.screen.ingame.HandledScreen

class InventoryPressEvent(val screen: HandledScreen<*>,
                          val keyCode: Int,
                          val scanCode: Int): CancellableEvent() {
    companion object: EventHandler.HandlerList<InventoryPressEvent>()
}