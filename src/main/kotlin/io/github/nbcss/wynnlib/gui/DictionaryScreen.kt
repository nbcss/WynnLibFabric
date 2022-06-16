package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.gui.widgets.ItemSlotWidget
import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.lang.Integer.max

abstract class DictionaryScreen<T: BaseItem>(title: Text) : HandbookTabScreen(title) {
    private val slotsBackground = Identifier("wynnlib", "textures/gui/dictionary_slots.png")
    protected val items: MutableList<T> = ArrayList()
    private val slots: MutableList<ItemSlotWidget<T>> = ArrayList()
    private var lineIndex: Int = 0
    private var lineSize: Int = 0
    protected abstract fun fetchItems(): Collection<T>

    override fun init() {
        super.init()
        items.clear()
        items.addAll(fetchItems())
        //Reset lines
        lineIndex = 0
        //Excluding 6 lines (only since 7th line need additional page)
        lineSize = max(0, (items.size + 8) / 9 - 6)
        //setup slots
        slots.clear()
        (0 until 54).forEach {
            val x = windowX + 6 + 24 * (it % 9)
            val y = 32 + windowY + 16 + 24 * (it / 9)
            val index = lineIndex * 9 + it
            val item = if(index < items.size) items[index] else null
            slots.add(ItemSlotWidget(x + 1, y + 1, item, this))
        }
    }

    override fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackground(matrices, mouseX, mouseY, delta)
        this.renderTexture(matrices, slotsBackground, windowX, windowY + 32,
            0, 0, this.backgroundWidth, this.backgroundHeight)
    }

    override fun drawContents(matrices: MatrixStack?,
                              mouseX: Int,
                              mouseY: Int,
                              delta: Float){
        //ButtonWidget
        slots.forEach{it.render(matrices, mouseX, mouseY, delta)}
    }
}