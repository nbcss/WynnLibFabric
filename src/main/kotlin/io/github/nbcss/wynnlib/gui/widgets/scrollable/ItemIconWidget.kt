package io.github.nbcss.wynnlib.gui.widgets.scrollable

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import java.util.function.Supplier

class ItemIconWidget(posX: Int, posY: Int,
                     private val itemProvider: Supplier<ItemStack>): BaseScrollableWidget(posX, posY) {
    companion object {
        private val renderer = MinecraftClient.getInstance().itemRenderer
    }

    constructor(posX: Int, posY: Int, item: ItemStack): this(posX, posY, Supplier {
        return@Supplier item
    })

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        itemProvider.get().let {
            renderer.renderInGui(it, getX(), getY())
        }
    }
}