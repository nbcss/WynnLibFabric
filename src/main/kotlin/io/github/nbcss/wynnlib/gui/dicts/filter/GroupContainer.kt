package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.gui.widgets.scrollable.ScrollListEntry
import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.util.math.MatrixStack

class GroupContainer<T: BaseItem> (val group: CriteriaGroup<T>): ScrollListEntry {
    private var top: Int = 0

    override fun updateTop(entryTop: Int) {
        this.top = entryTop
    }

    override fun getEntryHeight(): Int {
        return group.getHeight()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return group.getElements().any { it.mouseClicked(mouseX, mouseY, button) }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return group.getElements().any { it.mouseDragged(mouseX, mouseY, button, 0.0, 0.0) }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return group.getElements().any { it.mouseReleased(mouseX, mouseY, button) }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return group.getElements().any { it.keyPressed(keyCode, scanCode, modifiers) }
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return group.getElements().any { it.keyReleased(keyCode, scanCode, modifiers) }
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return group.getElements().any { it.charTyped(chr, modifiers) }
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        for (element in group.getElements()) {
            element.render(matrices, mouseX, mouseY, delta)
        }
    }

    override fun updateState(x: Int, y: Int, active: Boolean) {
        for (element in group.getElements()) {
            element.updateState(x, y + top, active)
        }
    }
}