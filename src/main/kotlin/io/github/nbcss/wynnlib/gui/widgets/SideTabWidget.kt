package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class SideTabWidget(private val posX: Int,
                    private val posY: Int,
                    private val icon: ItemStack,
                    private val side: Side,
                    private val index: Int): DrawableHelper() {
    private val itemRenderer: ItemRenderer = MinecraftClient.getInstance().itemRenderer
    companion object {
        private val TEXTURE = Identifier("wynnlib", "textures/gui/side_tabs.png")
        private const val TAB_HEIGHT = 28
        private const val TAB_WIDTH = 32

        fun fromWindowSide(windowX: Int, windowY: Int, offsetY: Int, side: Side, icon: ItemStack) {
            //todo
        }
    }

    fun isSelected(): Boolean {
        return false
    }

    fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if (!isSelected()) {
            val tabY = posY + index * TAB_HEIGHT
            RenderKit.renderTexture(matrices, TEXTURE, posX, tabY,
                32, 182, TAB_WIDTH, TAB_HEIGHT)
            itemRenderer.renderInGuiWithOverrides(icon, posX + 9, tabY + 6)
            if (isOverTab(mouseX, mouseY)){
                //drawTooltip(matrices, listOf(character.translate()), mouseX, mouseY)
            }
        }
    }

    fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if (isSelected()) {
            val tabY = posY + index * TAB_HEIGHT
            RenderKit.renderTexture(matrices, TEXTURE, posX, tabY,
                32, 210, TAB_WIDTH, TAB_HEIGHT)
            itemRenderer.renderInGuiWithOverrides(icon, posX + 9, tabY + 6)
            if (isOverTab(mouseX, mouseY)){
                //drawTooltip(matrices, listOf(character.translate()), mouseX, mouseY)
            }
        }
    }

    fun isOverTab(mouseX: Int, mouseY: Int): Boolean {
        return true
    }

    enum class Side {
        LEFT,
        RIGHT
    }
}