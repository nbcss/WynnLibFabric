package io.github.nbcss.wynnlib.gui.widgets

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element

interface ScrollElement: Element, Drawable {
    fun updatePosition(x: Int, y: Int)
}