package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class AdvanceSearchPaneWidget<T: BaseItem>(private val screen: DictionaryScreen<T>,
                                           x: Int,
                                           y: Int,
                                           width: Int,
                                           height: Int): ClickableWidget(x, y, width, height, null) {
    companion object {
        private val TEXTURE = Identifier("wynnlib", "textures/gui/search_filters.png")
    }
    private val close: ExitButtonWidget
    init {
        close = ExitButtonWidget(x + width - 15, y + 3, object: ExitButtonWidget.ExitHandler{
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
        RenderKit.renderTexture(
            matrices, TEXTURE, x, y, 0, 0, width, height
        )
        close.render(matrices, mouseX, mouseY, delta)
    }
}