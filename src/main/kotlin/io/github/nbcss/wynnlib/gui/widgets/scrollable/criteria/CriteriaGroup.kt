package io.github.nbcss.wynnlib.gui.widgets.scrollable.criteria

import io.github.nbcss.wynnlib.gui.widgets.scrollable.ScrollElement
import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.util.math.MatrixStack

abstract class CriteriaGroup<T: BaseItem>(val memory: CriteriaMemory<T>): ScrollElement {
    abstract fun onClick(mouseX: Int, mouseY: Int, button: Int): Boolean
    abstract fun render(
        matrices: MatrixStack,
        mouseX: Int,
        mouseY: Int,
        posX: Double,
        posY: Double,
        delta: Float,
        mouseOver: Boolean
    )
    abstract fun reload(memory: CriteriaMemory<T>)
    abstract fun getHeight(): Int

    override fun updateState(x: Int, y: Int, active: Boolean) {
        TODO("Not yet implemented")
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        TODO("Not yet implemented")
    }
}