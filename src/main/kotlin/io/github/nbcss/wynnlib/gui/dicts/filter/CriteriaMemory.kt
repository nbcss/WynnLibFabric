package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.function.Consumer

class CriteriaMemory<T: BaseItem>(private val onContentUpdate: Consumer<CriteriaMemory<T>>,
                                  private val onCriteriaUpdate: Consumer<CriteriaMemory<T>>,
                                  private val maxSorters: Int = 8) {
    private val filters: MutableMap<String, Filter<T>> = mutableMapOf()
    private val sorterIndexes: MutableMap<String, Int> = mutableMapOf()
    private val sorters: MutableList<Sorter<T>> = mutableListOf()
    fun handle(items: List<T>): List<T> {
        val copy: MutableList<Comparator<T>> = sorters.toMutableList()
        copy.add(Comparator.comparing { it.getDisplayName() })
        val comparator = copy
            .map { it as Comparator<T> }
            .reduce { acc, sorter ->  acc.thenComparing(sorter) }
        return items.filter { i -> filters.values.all { it.accept(i) } }
            .filter { i -> sorters.all { it.accept(i) } }.sortedWith(comparator)
    }

    fun getFilter(key: String): Filter<T>? {
        return filters[key]
    }

    fun putFilter(filter: Filter<T>) {
        filters[filter.getKey()] = filter
        onContentUpdate.accept(this)
    }

    fun removeFilter(key: String): Boolean {
        if (filters.remove(key) != null){
            onContentUpdate.accept(this)
            return true
        }
        return false
    }

    fun getSorter(key: String): Pair<Sorter<T>, Int>? {
        val index = sorterIndexes[key] ?: return null
        return sorters[index] to index
    }

    fun putSorter(sorter: Sorter<T>): Int? {
        val index = sorterIndexes[sorter.getKey()]
        return if (index != null) {
            sorters[index] = sorter
            onContentUpdate.accept(this)
            index
        }else if(sorterIndexes.size < maxSorters) {
            val size = sorterIndexes.size
            sorters.add(sorter)
            sorterIndexes[sorter.getKey()] = size
            size
        }else{
            null
        }
    }

    fun removeSorter(key: String): Boolean {
        val index = sorterIndexes.remove(key)
        if (index != null) {
            sorters.removeAt(index)
            for (i in (index until sorters.size)){
                sorterIndexes[sorters[i].getKey()] = i
            }
            onContentUpdate.accept(this)
            onCriteriaUpdate.accept(this)
            return true
        }
        return false
    }

    interface Filter<T>: Keyed {
        fun accept(item: T): Boolean
    }

    interface Sorter<T>: Filter<T>, Comparator<T> {
        override fun accept(item: T): Boolean = true
    }
}