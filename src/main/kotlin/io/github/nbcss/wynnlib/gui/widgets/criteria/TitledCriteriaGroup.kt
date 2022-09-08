package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

abstract class TitledCriteriaGroup<T: BaseItem>(memory: CriteriaMemory<T>): CriteriaGroup<T>(memory) {
    abstract fun renderContent(matrices: MatrixStack,
                               mouseX: Int,
                               mouseY: Int,
                               posX: Double,
                               posY: Double,
                               delta: Float)

    abstract fun getContentHeight(): Int

    abstract fun getTitle(): Text

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float) {
        val titleX = posX.toInt() + 2
        val titleY = posY.toInt() + 3
        RenderKit.renderOutlineText(matrices, getTitle(), titleX.toFloat(), titleY.toFloat())
        renderContent(matrices, mouseX, mouseY, posX, posY + 12.0, delta)
    }

    override fun getHeight(): Int {
        return getContentHeight() + 12
    }
}