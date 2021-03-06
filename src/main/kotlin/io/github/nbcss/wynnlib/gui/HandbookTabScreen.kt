package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.ability.AbilityTreeViewerScreen
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.gui.dicts.IngredientDictScreen
import io.github.nbcss.wynnlib.gui.dicts.MaterialDictScreen
import io.github.nbcss.wynnlib.gui.dicts.PowderDictScreen
import io.github.nbcss.wynnlib.gui.widgets.ExitButtonWidget
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class HandbookTabScreen(val parent: Screen?, title: Text?) : Screen(title),
    TooltipScreen, ExitButtonWidget.ExitHandler {
    private val texture = Identifier("wynnlib", "textures/gui/handbook_tab.png")
    companion object {
        const val TAB_SIZE: Int = 7
    }
    protected val backgroundWidth = 246
    protected val backgroundHeight = 210
    protected val tabs: MutableList<TabFactory> = ArrayList()
    private var tabPage: Int = 0
    protected var exitButton: ExitButtonWidget? = null
    protected var windowWidth = backgroundWidth
    protected var windowHeight = backgroundHeight
    protected var windowX = backgroundWidth
    protected var windowY = backgroundHeight
    init {
        //setup default tabs
        tabs.add(EquipmentDictScreen.FACTORY)
        tabs.add(IngredientDictScreen.FACTORY)
        //tabs.add(CrafterScreen.FACTORY)
        tabs.add(AbilityTreeViewerScreen.FACTORY)
        tabs.add(PowderDictScreen.FACTORY)
        tabs.add(MaterialDictScreen.FACTORY)
        //tabs.add(ConfigurationScreen.FACTORY)
    }

    override fun init() {
        super.init()
        windowWidth = backgroundWidth
        windowHeight = backgroundHeight
        windowX = (width - windowWidth) / 2
        windowY = (height - windowHeight) / 2
        val closeX = windowX + 230
        val closeY = windowY + 32
        exitButton = addDrawableChild(ExitButtonWidget(this, closeX, closeY))
    }

    open fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val tabIndex = tabPage * TAB_SIZE
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .filter{!tabs[tabIndex + it].isInstance(this)}
            .forEach { drawTab(matrices!!, tabs[tabIndex + it], it, mouseX, mouseY) }
    }

    open fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val tabIndex = tabPage * TAB_SIZE
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .filter{tabs[tabIndex + it].isInstance(this)}
            .forEach { drawTab(matrices!!, tabs[tabIndex + it], it, mouseX, mouseY) }
    }

    open fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawBackgroundPre(matrices, mouseX, mouseY, delta)
        //render background
        RenderKit.renderTexture(
            matrices, texture, windowX, windowY, 0, 0,
            backgroundWidth, backgroundHeight
        )
        //render selected tab (normally should only have up to one tab)
        drawBackgroundPost(matrices, mouseX, mouseY, delta)
        textRenderer.draw(
            matrices, getTitle().asOrderedText(),
            (windowX + 6).toFloat(),
            (windowY + 34).toFloat(), 0
        )
    }

    private fun drawTab(matrices: MatrixStack, tab: TabFactory, tabIndex: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 25 + tabIndex * 28
        val u = if (tab.isInstance(this)) 0 else 28
        RenderKit.renderTexture(matrices, texture, posX, windowY, u, 210, 28, 32)
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

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true
        }else if (this.client!!.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            client!!.setScreen(parent)
            return true
        }
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val tabIndex = tabPage * TAB_SIZE
        (0 until TAB_SIZE).filter{tabIndex + it < tabs.size}
            .firstOrNull {isOverTab(it, mouseX.toInt(), mouseY.toInt())}?.let {
                val tab = tabs[tabIndex + it]
                client!!.setScreen(tab.createScreen(parent))
                playSound(SoundEvents.ITEM_BOOK_PAGE_TURN)
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

    override fun exit() {
        client!!.setScreen(parent)
    }

    override fun drawTooltip(matrices: MatrixStack, tooltip: List<Text>, x: Int, y: Int) {
        matrices.push()
        renderOrderedTooltip(matrices, tooltip.map{it.asOrderedText()}, x, y)
        RenderSystem.enableDepthTest()
        matrices.pop()
    }
}