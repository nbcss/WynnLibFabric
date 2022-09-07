package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.widgets.criteria.CriteriaMemory
import io.github.nbcss.wynnlib.gui.widgets.criteria.CriteriaGroup
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class AdvanceSearchPaneWidget<T: BaseItem>(private val screen: DictionaryScreen<T>,
                                           private val criteriaList: List<CriteriaGroup<T>>,
                                           x: Int,
                                           y: Int): ClickableWidget(x, y, WIDTH, HEIGHT, null) {
    companion object {
        private val TEXTURE = Identifier("wynnlib", "textures/gui/search_filters.png")
        private val SLIDER_TEXTURE = TextureData(TEXTURE, 148)
        private val client = MinecraftClient.getInstance()
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

    fun reload(memory: CriteriaMemory<T>) {
        criteriaList.forEach { it.reload(memory) }
    }

    fun getCriteriaList(): List<CriteriaGroup<T>> = criteriaList

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
        client.textRenderer.draw(matrices, Translations.UI_ADVANCE_SEARCH.translate(),
            x.toFloat() + 6.0f, y.toFloat() + 4.0f, Color.BLACK.code())
        close.render(matrices, mouseX, mouseY, delta)
        slider.render(matrices, mouseX, mouseY, delta)
        scroll.render(matrices, mouseX, mouseY, delta)
    }

    inner class Scroll: ScrollPaneWidget(null, screen,
        x + 8, y + 15, width - 30, height - 24, 200L, 20.0) {

        override fun getSlider(): VerticalSliderWidget = slider

        override fun renderContents(matrices: MatrixStack, mouseX: Int, mouseY: Int, position: Double, delta: Float) {
            //fill(matrices, x, y, x + width, y + height, 0x1DA1AAFF)
            val posX = x.toDouble()
            var posY = y - position
            getCriteriaList().forEach {
                it.render(matrices, mouseX, mouseY, posX, posY, delta)
                posY += it.getHeight()
            }
        }

        override fun getContentHeight(): Int {
            return getCriteriaList().sumOf { it.getHeight() }
        }
    }
}