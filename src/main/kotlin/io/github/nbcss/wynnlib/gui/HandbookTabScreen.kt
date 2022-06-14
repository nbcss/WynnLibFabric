package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

abstract class HandbookTabScreen(title: Text?) : Screen(title) {
    private val background = Identifier("wynnlib", "textures/gui/handbook_tab.png")
    protected var backgroundWidth = 256
    protected var backgroundHeight = 192

    override fun init() {
        super.init()
        backgroundWidth = 256
        backgroundHeight = 192
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, background)
        val x: Int = (width - this.backgroundWidth) / 2
        val y: Int = (height - this.backgroundHeight) / 2
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight)
        textRenderer.draw(matrices, getTitle().asOrderedText(),
            (x + 10).toFloat(),
            (y + 6).toFloat(), 0)
        super.render(matrices, mouseX, mouseY, delta)
    }
}