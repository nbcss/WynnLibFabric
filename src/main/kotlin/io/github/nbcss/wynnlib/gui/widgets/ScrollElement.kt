package io.github.nbcss.wynnlib.gui.widgets

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element

interface ScrollElement: Element, Drawable {
    fun updateState(x: Int, y: Int, active: Boolean)
}