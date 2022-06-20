package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.gui.widgets.ItemSlotWidget
import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import java.lang.Integer.max

abstract class DictionaryScreen<T: BaseItem>(parent: Screen?, title: Text) : HandbookTabScreen(parent, title) {
    companion object {
        const val COLUMNS = 9
        const val ROWS = 6
    }
    private val slotsBackground = Identifier("wynnlib", "textures/gui/dictionary_slots.png")
    protected val items: MutableList<T> = ArrayList()
    private val slots: MutableList<ItemSlotWidget<T>> = ArrayList()
    private var lineIndex: Int = 0
    private var lineSize: Int = 0
    private var sliderLength: Double = 1.0
    protected abstract fun fetchItems(): Collection<T>

    init {

    }

    override fun init() {
        super.init()
        items.clear()
        items.addAll(fetchItems())
        items.sortBy { t -> t.getDisplayName() }
        //Reset lines
        lineIndex = 0
        //Excluding 6 lines (only since 7th line need additional page)
        lineSize = max(0, (items.size + (COLUMNS - 1)) / COLUMNS - ROWS)
        //setup slots
        slots.clear()
        (0 until (ROWS * COLUMNS)).forEach {
            val x = windowX + 6 + 24 * (it % COLUMNS)
            val y = windowY + 44 + 24 * (it / COLUMNS)
            slots.add(ItemSlotWidget(x + 1, y + 1, 22, null, this))
        }
        //update items in slots
        updateSlots()
    }

    fun updateSlots() {
        (0 until (ROWS * COLUMNS)).forEach {
            val index = lineIndex * COLUMNS + it
            val item = if(index < items.size) items[index] else null
            slots[it].setItem(item)
        }
    }

    override fun getTitle(): Text {
        return title.copy().append(LiteralText(" [${items.size}]"))
    }

    override fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackground(matrices, mouseX, mouseY, delta)
        this.renderTexture(matrices, slotsBackground, windowX, windowY + 32,
            0, 0, this.backgroundWidth, this.backgroundHeight)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        //println("${keyCode}, ${scanCode}, $modifiers")
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        //println("${mouseX}, ${mouseY}, $amount")
        if(isInPage(mouseX, mouseY)){
            lineIndex = MathHelper.clamp(lineIndex - amount.toInt(), 0, lineSize)
            updateSlots()
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun drawContents(matrices: MatrixStack?,
                              mouseX: Int,
                              mouseY: Int,
                              delta: Float){
        //ButtonWidget
        slots.forEach{it.render(matrices, mouseX, mouseY, delta)}
        //println(isInPage(mouseX, mouseY))
    }

    private fun isInPage(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= windowX + 6 && mouseY >= windowY + 44 && mouseX <= windowX + 222 && mouseY <= windowY + 188
    }
}