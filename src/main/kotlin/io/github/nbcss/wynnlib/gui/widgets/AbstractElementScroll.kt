package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.render.TextureData
import net.minecraft.client.gui.Element

abstract class AbstractElementScroll(background: TextureData?,
                                     screen: TooltipScreen,
                                     x: Int,
                                     y: Int,
                                     width: Int,
                                     height: Int,
                                     scrollDelay: Long = 200L,
                                     scrollUnit: Double = 32.0):
    ScrollPaneWidget(background, screen, x, y, width, height, scrollDelay, scrollUnit) {

    abstract fun getElements(): List<Element>

    override fun onContentDrag(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return getElements().any { it.mouseDragged(mouseX, mouseY, button, 0.0, 0.0) }
    }

    override fun onContentRelease(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return getElements().any { it.mouseReleased(mouseX, mouseY, button) }
    }

    override fun onContentClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return getElements().any { it.mouseClicked(mouseX, mouseY, button) }
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return getElements().any { it.keyReleased(keyCode, scanCode, modifiers) }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return getElements().any { it.keyPressed(keyCode, scanCode, modifiers) }
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return getElements().any { it.charTyped(chr, modifiers) }
    }
}