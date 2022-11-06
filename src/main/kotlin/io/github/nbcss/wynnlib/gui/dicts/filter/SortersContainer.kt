package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.gui.dicts.sorter.SorterGroup

class SortersContainer<T: BaseItem>(memory: CriteriaState<T>,
                                    sorters: List<SorterGroup>): CriteriaGroup<T>(memory) {

    init {

    }

    override fun reload(memory: CriteriaState<T>) {
        TODO("Not yet implemented")
    }
    override fun getHeight(): Int {
        TODO("Not yet implemented")
    }

    class Builder {

    }
}