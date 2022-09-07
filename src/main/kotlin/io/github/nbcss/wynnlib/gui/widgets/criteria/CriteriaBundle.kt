package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Keyed

class CriteriaBundle<T: BaseItem> {
    private val filters: MutableMap<String, Filter<T>> = mutableMapOf()
    private val sorterIndexes: MutableMap<String, Int> = mutableMapOf()
    private val sorters: MutableList<Sorter<T>> = mutableListOf()
    fun handle(items: List<T>): List<T> {
        return items.filter { i -> filters.values.all { it.accept(i) } }
    }

    fun getFilter(key: String): Filter<T>? {
        return filters[key]
    }

    fun putFilter(filter: Filter<T>) {
        filters[filter.getKey()] = filter
    }

    fun getSorter(key: String): Pair<Sorter<T>, Int>? {
        val index = sorterIndexes[key] ?: return null
        return sorters[index] to index
    }

    fun putSorter(sorter: Sorter<T>): Int? {
        return 0
    }

    interface Filter<T>: Keyed {
        fun accept(item: T): Boolean
    }

    interface Sorter<T>: Filter<T> {
        fun compare(item1: T, item2: T): Int
    }
}