package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.gui.widgets.ItemSlotWidget
import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class DictionaryScreen<T: BaseItem>(title: Text) : HandbookTabScreen(title) {
    private val slotsBackground = Identifier("wynnlib", "textures/gui/dictionary_slots.png")
    protected val items: MutableList<T> = ArrayList()
    private val slots: MutableList<ItemSlotWidget<T>> = ArrayList()
    private var lineIndex: Int = 0
    protected abstract fun fetchItems(): Collection<T>

    override fun init() {
        super.init()
        items.clear()
        items.addAll(fetchItems())
        //setup slots
        slots.clear()
        (0 until 54).forEach {
            val x = windowX + 6 + 24 * (it % 9)
            val y = windowY + 16 + 24 * (it / 9)
            slots.add(ItemSlotWidget(x, y, items.first()))
        }
    }

    override fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackground(matrices, mouseX, mouseY, delta)
        this.renderTexture(matrices, slotsBackground, windowX, windowY,
            0, 0, this.backgroundWidth, this.backgroundHeight)
    }

    override fun drawContents(matrices: MatrixStack?,
                              mouseX: Int,
                              mouseY: Int,
                              delta: Float){
        //ButtonWidget
        slots.forEach{it.renderButton(matrices, mouseX, mouseY, delta)}
    }
}