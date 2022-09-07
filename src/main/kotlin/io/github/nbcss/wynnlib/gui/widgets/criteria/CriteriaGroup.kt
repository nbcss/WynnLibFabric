package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.util.math.MatrixStack

interface CriteriaGroup<T: BaseItem> {
    fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float)
    fun reload(memory: CriteriaMemory<T>)
    fun getHeight(): Int
}