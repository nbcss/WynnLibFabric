package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.util.math.MatrixStack

class AdvanceSearchPaneWidget<T: BaseItem>(private val screen: DictionaryScreen<T>,
                                           x: Int,
                                           y: Int,
                                           width: Int,
                                           height: Int): ClickableWidget(x, y, width, height, null) {
    private val close: ExitButtonWidget
    init {
        close = ExitButtonWidget(x + 107, y + 2, object: ExitButtonWidget.ExitHandler{
            override fun exit() {
                screen.setFilterVisible(false)
            }
        })
    }

    fun filter(item: T): Boolean {
        return true
    }

    fun compare(item1: T, item2: T): Int {
        return 0
    }

    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (close.mouseClicked(mouseX, mouseY, button))
            return true
        return false
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //screen.setFilterVisible(false)
        DrawableHelper.fill(matrices, x, y, x + width, y + height, Color.WHITE.solid().code())
        close.render(matrices, mouseX, mouseY, delta)
    }
}