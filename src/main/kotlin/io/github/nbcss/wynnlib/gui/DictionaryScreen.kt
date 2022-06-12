package io.github.nbcss.wynnlib.gui

import net.minecraft.text.LiteralText

abstract class DictionaryScreen<T> : AbstractHandbookScreen(LiteralText("Test")) {
    protected val items: List<T> = ArrayList()
}