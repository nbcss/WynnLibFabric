package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.gui.widgets.ScrollPaneWidget
import io.github.nbcss.wynnlib.gui.widgets.VerticalSliderWidget
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class GenericScrollScreen(parent: Screen?, title: Text) : HandbookTabScreen(parent, title) {
    companion object {
        val TEXTURE: Identifier = Identifier("wynnlib", "textures/gui/generic_ui.png")
        val SLIDER_TEXTURE = TextureData(TEXTURE, 246)
        const val SCROLL_WIDTH = 212
        const val SCROLL_HEIGHT = 156
        const val SLIDER_WIDTH = 10
        const val BAR_HEIGHT = 30
    }
    protected var client: MinecraftClient = MinecraftClient.getInstance()
    protected var slider: VerticalSliderWidget? = null
    protected var scrollX: Int = windowX + 12
    protected var scrollY: Int = windowY + 44
    abstract fun getScroll(): ScrollPaneWidget?

    override fun init() {
        super.init()
        scrollX = windowX + 12
        scrollY = windowY + 44
        slider = VerticalSliderWidget(windowX + 225, windowY + 44,
            SLIDER_WIDTH, SCROLL_HEIGHT, BAR_HEIGHT, SLIDER_TEXTURE){
            getScroll()?.let { scroll ->
                scroll.setScrollPosition(it * scroll.getMaxPosition())
            }
        }
    }

    override fun drawBackgroundTexture(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        RenderKit.renderTexture(
            matrices, TEXTURE, windowX, windowY + 28, 0, 0,
            backgroundWidth, 182
        )
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (getScroll()?.mouseScrolled(mouseX, mouseY, amount) == true)
            return true
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (getScroll()?.mouseDragged(mouseX, mouseY, button, 0.0, 0.0) == true)
            return true
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (getScroll()?.mouseClicked(mouseX, mouseY, button) == true)
            return true
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (getScroll()?.mouseReleased(mouseX, mouseY, button) == true)
            return true
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        getScroll()?.render(matrices, mouseX, mouseY, delta)
    }
}