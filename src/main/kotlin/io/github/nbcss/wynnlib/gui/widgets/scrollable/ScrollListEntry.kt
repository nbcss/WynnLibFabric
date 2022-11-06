package io.github.nbcss.wynnlib.gui.widgets.scrollable

interface ScrollListEntry: ScrollElement {
    fun updateTop(entryTop: Int)
    fun getEntryHeight(): Int
}