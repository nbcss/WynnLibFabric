package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.gui.widgets.scrollable.ScrollElement
import io.github.nbcss.wynnlib.items.BaseItem

abstract class FilterGroup<T: BaseItem>(val memory: CriteriaMemory<T>) {
    private val elements: MutableList<ScrollElement> = mutableListOf()

    //abstract fun onClick(mouseX: Int, mouseY: Int, button: Int): Boolean
    /*abstract fun render(
        matrices: MatrixStack,
        mouseX: Int,
        mouseY: Int,
        posX: Double,
        posY: Double,
        delta: Float,
        mouseOver: Boolean
    )*/

    fun addElement(element: ScrollElement){
        elements.add(element)
    }

    fun getElements(): List<ScrollElement> = elements

    abstract fun reload(memory: CriteriaMemory<T>)

    abstract fun getHeight(): Int
}