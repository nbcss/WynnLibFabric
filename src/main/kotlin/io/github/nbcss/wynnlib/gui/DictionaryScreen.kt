package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.text.LiteralText

abstract class DictionaryScreen<T: BaseItem> : AbstractHandbookScreen(LiteralText("Test")) {
    protected val items: List<T> = ArrayList()
}