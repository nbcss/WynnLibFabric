package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.items.BaseItem

abstract class FilterGroup<T: BaseItem>(memory: CriteriaState<T>): CriteriaGroup<T>(memory)
