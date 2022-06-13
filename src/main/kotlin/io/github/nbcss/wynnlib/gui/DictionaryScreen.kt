package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.text.LiteralText

abstract class DictionaryScreen<T: BaseItem> : HandbookTabScreen(LiteralText("Test")) {
    protected val items: MutableList<T> = ArrayList()
    protected abstract fun fetchItems(): Collection<T>

    override fun init() {
        super.init()
        items.clear()
        items.addAll(fetchItems())
    }
}