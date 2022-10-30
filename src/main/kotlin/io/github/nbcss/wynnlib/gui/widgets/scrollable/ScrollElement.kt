package io.github.nbcss.wynnlib.gui.widgets.scrollable

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element

interface ScrollElement: Element, Drawable {
    fun updateState(x: Int, y: Int, active: Boolean)
}