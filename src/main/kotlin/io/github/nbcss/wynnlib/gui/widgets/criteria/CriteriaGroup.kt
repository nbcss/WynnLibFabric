package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.util.math.MatrixStack

abstract class CriteriaGroup<T: BaseItem>(val memory: CriteriaMemory<T>) {
    abstract fun onClick(mouseX: Int, mouseY: Int, button: Int): Boolean
    abstract fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float)
    abstract fun reload(memory: CriteriaMemory<T>)
    abstract fun getHeight(): Int
}