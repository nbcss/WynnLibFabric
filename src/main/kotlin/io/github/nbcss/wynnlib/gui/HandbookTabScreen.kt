package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.gui.dicts.IngredientDictScreen
import io.github.nbcss.wynnlib.gui.dicts.MaterialDictScreen
import io.github.nbcss.wynnlib.gui.dicts.PowderDictScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class HandbookTabScreen(val parent: Screen, title: Text?) : Screen(title), TooltipScreen {
    private val background = Identifier("wynnlib", "textures/gui/handbook_tab.png")
    companion object {
        const val TAB_SIZE: Int = 7
    }
    protected val backgroundWidth = 246
    protected val backgroundHeight = 210
    protected val tabs: MutableList<TabFactory> = ArrayList()
    private var tabPage: Int = 0
    protected var windowWidth = backgroundWidth
    protected var windowHeight = backgroundHeight
    protected var windowX = backgroundWidth
    protected var windowY = backgroundHeight
    init {
        //setup default tabs
        tabs.add(EquipmentDictScreen.FACTORY)
        tabs.add(IngredientDictScreen.FACTORY)
        tabs.add(PowderDictScreen.FACTORY)
        tabs.add(MaterialDictScreen.FACTORY)
    }

    override fun init() {
        super.init()
        windowWidth = backgroundWidth
        windowHeight = backgroundHeight
        windowX = (width - windowWidth) / 2
        windowY = (height - windowHeight) / 2
    }

    open fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        val tabIndex = tabPage * TAB_SIZE
        //render not-selected tabs first
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .filter{!tabs[tabIndex + it].isInstance(this)}
            .forEach { drawTab(matrices!!, tabs[tabIndex + it], it, mouseX, mouseY) }
        //render background
        this.renderTexture(
            matrices, background, windowX, windowY, 0, 0,
            backgroundWidth, backgroundHeight
        )
        //render selected tab (normally should only have up to one tab)
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .filter{tabs[tabIndex + it].isInstance(this)}
            .forEach { drawTab(matrices!!, tabs[tabIndex + it], it, mouseX, mouseY) }
        textRenderer.draw(
            matrices, getTitle().asOrderedText(),
            (windowX + 6).toFloat(),
            (windowY + 34).toFloat(), 0
        )
    }

    private fun drawTab(matrices: MatrixStack, tab: TabFactory, tabIndex: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 25 + tabIndex * 28
        val u = if (tab.isInstance(this)) 0 else 28
        this.renderTexture(matrices, background, posX, windowY, u, 210, 28, 32)
        itemRenderer.renderInGuiWithOverrides(tab.getTabIcon(), posX + 6, windowY + 9)
        itemRenderer.renderGuiItemOverlay(textRenderer, tab.getTabIcon(), posX + 6, windowY + 9)
        if(isOverTab(tabIndex, mouseX, mouseY)){
            drawTooltip(matrices, tab.getTabTooltip(), mouseX, mouseY)
        }
    }

    private fun isOverTab(tabIndex: Int, mouseX: Int, mouseY: Int): Boolean {
        val posX = windowX + 25 + tabIndex * 28
        return mouseX >= posX && mouseX < posX + 28 && mouseY >= windowY && mouseY <= windowY + 28
    }

    abstract fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float)

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val tabIndex = tabPage * TAB_SIZE
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .firstOrNull {isOverTab(it, mouseX.toInt(), mouseY.toInt())}?.let {
                val tab = tabs[tabIndex + it]
                client!!.setScreen(tab.createScreen(parent))
                return true
            }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        drawBackground(matrices, mouseX, mouseY, delta)
        super.render(matrices, mouseX, mouseY, delta)
        drawContents(matrices, mouseX, mouseY, delta)
    }

    override fun shouldPause(): Boolean = false

    override fun drawTooltip(matrices: MatrixStack, tooltip: List<Text>, x: Int, y: Int) {
        matrices.push()
        renderOrderedTooltip(matrices, tooltip.map{it.asOrderedText()}, x, y)
        RenderSystem.enableDepthTest()
        matrices.pop()
    }

    fun renderTexture(
        matrices: MatrixStack?, texture: Identifier,
        x: Int, y: Int, u: Int, v: Int, width: Int, height: Int
    ) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, texture)
        this.drawTexture(matrices, x, y, u, v, width, height)
    }
}