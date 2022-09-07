package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.widgets.criteria.SearchCriteriaWidget
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class AdvanceSearchPaneWidget<T: BaseItem>(private val screen: DictionaryScreen<T>,
                                           x: Int,
                                           y: Int): ClickableWidget(x, y, WIDTH, HEIGHT, null) {
    companion object {
        private val TEXTURE = Identifier("wynnlib", "textures/gui/search_filters.png")
        private val SLIDER_TEXTURE = TextureData(TEXTURE, 148)
        const val WIDTH = 148
        const val HEIGHT = 184
    }
    private val close: ExitButtonWidget
    private val slider: VerticalSliderWidget
    private val scroll: Scroll
    init {
        close = ExitButtonWidget(x + width - 15, y + 3, object: ExitButtonWidget.ExitHandler{
            override fun exit() {
                screen.setFilterVisible(false)
            }
        })
        scroll = Scroll()
        slider = VerticalSliderWidget(x + width - 20, y + 17, 10, 155, 30,
            SLIDER_TEXTURE){
            scroll.setScrollPosition(it * scroll.getMaxPosition())
        }
    }

    fun filter(item: T): Boolean {
        return true
    }

    fun compare(item1: T, item2: T): Int {
        return 0
    }

    fun getCriteriaList(): List<SearchCriteriaWidget<T>> = emptyList()

    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (close.mouseClicked(mouseX, mouseY, button))
            return true
        if (slider.mouseClicked(mouseX, mouseY, button))
            return true
        if (scroll.mouseClicked(mouseX, mouseY, button))
            return true
        return false
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (slider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
            return true
        if (scroll.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
            return true
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (slider.mouseReleased(mouseX, mouseY, button))
            return true
        if (scroll.mouseReleased(mouseX, mouseY, button))
            return true
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (scroll.mouseScrolled(mouseX, mouseY, amount))
            return true
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //screen.setFilterVisible(false)
        RenderKit.renderTexture(
            matrices, TEXTURE, x, y, 0, 0, width, height
        )
        close.render(matrices, mouseX, mouseY, delta)
        slider.render(matrices, mouseX, mouseY, delta)
        scroll.render(matrices, mouseX, mouseY, delta)
    }

    inner class Scroll: ScrollPaneWidget(null, screen,
        x + 8, y + 15, width - 30, height - 24) {

        override fun getSlider(): VerticalSliderWidget = slider

        override fun renderContents(matrices: MatrixStack, mouseX: Int, mouseY: Int, position: Double, delta: Float) {
            //fill(matrices, x, y, x + width, y + height, 0x1DA1AAFF)
        }

        override fun getContentHeight(): Int {
            return getCriteriaList().sumOf { it.getHeight() }
        }
    }
}