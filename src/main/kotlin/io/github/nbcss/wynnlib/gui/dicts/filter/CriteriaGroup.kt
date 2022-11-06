package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.gui.widgets.scrollable.ScrollElement
import io.github.nbcss.wynnlib.items.BaseItem

abstract class CriteriaGroup<T: BaseItem>(val memory: CriteriaState<T>) {
    private val elements: MutableList<ScrollElement> = mutableListOf()

    fun addElement(element: ScrollElement){
        elements.add(element)
    }

    fun getElements(): List<ScrollElement> = elements

    abstract fun reload(memory: CriteriaState<T>)

    abstract fun getHeight(): Int
}