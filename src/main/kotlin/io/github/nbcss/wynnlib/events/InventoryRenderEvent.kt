package io.github.nbcss.wynnlib.events

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

data class InventoryRenderEvent(val screen: HandledScreen<*>,
                                val matrices: MatrixStack,
                                val screenX: Int,
                                val screenY: Int,
                                val mouseX: Int,
                                val mouseY: Int,
                                val delta: Float,
                                val phase: Phase) {
    companion object: EventHandler.HandlerList<InventoryRenderEvent>()
    enum class Phase {
        PRE, POST
    }
}