package io.github.nbcss.wynnlib.events

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

data class RenderItemOverrideEvent(val matrixStack: MatrixStack,
                                   val renderer: TextRenderer,
                                   val item: ItemStack,
                                   val x: Int,
                                   val y: Int): CancellableEvent(){
    companion object: EventHandler.HandlerList<RenderItemOverrideEvent>()
}