package io.github.nbcss.wynnlib.events

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.slot.Slot

class SlotClickEvent(val screen: HandledScreen<*>,
                     val slot: Slot,
                     val button: Int): CancellableEvent() {
    companion object: EventHandler.HandlerList<SlotClickEvent>()
}