package io.github.nbcss.wynnlib.events

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.slot.Slot

class DrawSlotEvent(val screen: HandledScreen<*>,
                    val matrices: MatrixStack,
                    val slot: Slot) {
    companion object: EventHandler.HandlerList<DrawSlotEvent>()
}