package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class HandbookTabScreen(title: Text?) : Screen(title), TooltipScreen {
    private val background = Identifier("wynnlib", "textures/gui/handbook_tab.png")
    protected val backgroundWidth = 246
    protected val backgroundHeight = 214
    protected var windowWidth = backgroundWidth
    protected var windowHeight = backgroundHeight
    protected var windowX = backgroundWidth
    protected var windowY = backgroundHeight

    override fun init() {
        super.init()
        windowWidth = backgroundWidth
        windowHeight = backgroundHeight
        windowX = (width - windowWidth) / 2
        windowY = (height - windowHeight) / 2
    }

    open fun drawBackground(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderTexture(
            matrices, background, windowX, windowY, 0, 0,
            backgroundWidth, backgroundHeight
        )
        textRenderer.draw(
            matrices, getTitle().asOrderedText(),
            (windowX + 6).toFloat(),
            (windowY + 38).toFloat(), 0
        )
    }

    abstract fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float)

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        drawBackground(matrices, mouseX, mouseY, delta)
        super.render(matrices, mouseX, mouseY, delta)
        drawContents(matrices, mouseX, mouseY, delta)
    }

    override fun shouldPause(): Boolean = false

    override fun drawTooltip(matrices: MatrixStack, tooltip: List<Text>, x: Int, y: Int) {
        matrices.push()
        //matrices.translate(0.0, 0.0, 100.0)
        renderOrderedTooltip(matrices, tooltip.map{it.asOrderedText()}, x, y)
        RenderSystem.disableDepthTest()
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