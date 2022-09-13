package io.github.nbcss.wynnlib.events

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.slot.Slot

class SlotPressEvent(val screen: HandledScreen<*>,
                     val slot: Slot,
                     val keyCode: Int,
                     val scanCode: Int): CancellableEvent() {
    companion object: EventHandler.HandlerList<SlotPressEvent>()
}