package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.gui.widgets.ItemSearchWidget
import io.github.nbcss.wynnlib.gui.widgets.ItemSlotWidget
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.roundToInt

abstract class DictionaryScreen<T: BaseItem>(parent: Screen?, title: Text) : HandbookTabScreen(parent, title) {
    companion object {
        const val COLUMNS = 9
        const val ROWS = 6
    }
    private val slotsBackground = Identifier("wynnlib", "textures/gui/dictionary_slots.png")
    protected val items: MutableList<T> = ArrayList()
    private val slots: MutableList<ItemSlotWidget<T>> = ArrayList()
    private var lastSearch: String = ""
    private var searchBox: ItemSearchWidget? = null
    private var lineIndex: Int = 0
    private var lineSize: Int = 0
    private var sliderLength: Double = 1.0
    private var initialized = false
    private var barTop: Int = 0
    private var barBottom: Int = 1
    protected abstract fun fetchItems(): Collection<T>

    override fun init() {
        super.init()
        searchBox = ItemSearchWidget(textRenderer, windowX + 22, windowY + 191, 120, 12)
        searchBox!!.text = lastSearch
        searchBox!!.isFocused = true
        searchBox!!.setChangedListener{onSearchChanged(it)}
        focused = addDrawableChild(searchBox!!)
        if (!initialized) {
            updateItems()
            //Reset lines
            lineIndex = 0
            //Excluding 6 lines (only since 7th line need additional page)
            lineSize = max(0, (items.size + (COLUMNS - 1)) / COLUMNS - ROWS)
            initialized = true
        }
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

    private fun updateItems() {
        items.clear()
        items.addAll(fetchItems().filter { searchBox!!.validate(it) })
        items.sortBy { t -> t.getDisplayName() }
    }

    private fun updateSlots() {
        (0 until (ROWS * COLUMNS)).forEach {
            val index = lineIndex * COLUMNS + it
            val item = if(index < items.size) items[index] else null
            slots[it].setItem(item)
        }
        val barHeight: Float = max(ROWS / (ROWS + lineSize).toFloat(), 0.1f)
        val barPos: Float = if (barHeight == 1.0f) 0.0f else (1 - barHeight) / lineSize * lineIndex
        val y1 = windowY + 45
        val y2 = windowY + 187
        barTop = y1 + ((y2 - y1) * barPos).roundToInt()
        barBottom = barTop + ((y2 - y1) * barHeight).roundToInt()
    }

    override fun getTitle(): Text {
        return title.copy().append(LiteralText(" [${items.size}]"))
    }

    override fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackground(matrices, mouseX, mouseY, delta)
        RenderKit.renderTexture(matrices, slotsBackground, windowX, windowY + 32,
            0, 0, this.backgroundWidth, this.backgroundHeight)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        //println("$mouseX $mouseY $button $deltaX $deltaY")
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
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

    override fun tick() {
        super.tick()
        searchBox!!.tick()
    }

    override fun drawContents(matrices: MatrixStack?,
                              mouseX: Int,
                              mouseY: Int,
                              delta: Float){
        //slide bar
        val x1 = windowX + 227
        val x2 = windowX + 239
        DrawableHelper.fill(matrices, x1, barTop, x2, barBottom, Color.DARK_GRAY.toSolidColor().getColorCode())
        //ButtonWidget
        slots.forEach{it.render(matrices, mouseX, mouseY, delta)}
    }

    private fun isInPage(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= windowX + 6 && mouseY >= windowY + 44 && mouseX <= windowX + 240 && mouseY <= windowY + 188
    }

    fun onSearchChanged(text: String) {
        if (text == lastSearch)
            return
        lastSearch = text
        updateItems()
        //Reset lines
        lineIndex = 0
        //Excluding 6 lines (only since 7th line need additional page)
        lineSize = max(0, (items.size + (COLUMNS - 1)) / COLUMNS - ROWS)
        updateSlots()
    }
}