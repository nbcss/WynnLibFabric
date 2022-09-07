package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.items.BaseItem

class SearchCriteriaWidget<T: BaseItem> {
    fun filter(item: T): Boolean {
        return true
    }

    fun compare(item1: T, item2: T): Int {
        return 0
    }

    fun getHeight(): Int {
        return 100
    }
}