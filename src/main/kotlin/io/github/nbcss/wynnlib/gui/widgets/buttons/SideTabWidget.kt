package io.github.nbcss.wynnlib.gui.widgets.buttons

import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

class SideTabWidget(private val index: Int,
                    private val posX: Int,
                    private val posY: Int,
                    private val icon: ItemStack,
                    private val side: Side,
                    private val handler: Handler
): DrawableHelper() {
    private val itemRenderer: ItemRenderer = MinecraftClient.getInstance().itemRenderer
    companion object {
        private val TEXTURE = Identifier("wynnlib", "textures/gui/tab_buttons.png")
        private const val TAB_HEIGHT = 28
        private const val TAB_WIDTH = 32

        fun fromWindowSide(index: Int,
                           windowX: Int,
                           windowY: Int,
                           offsetY: Int,
                           side: Side,
                           icon: ItemStack,
                           handler: Handler
        ): SideTabWidget {
            val posX = windowX + side.windowX
            val posY = windowY + offsetY
            return SideTabWidget(index, posX, posY, icon, side, handler)
        }
    }

    fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int) {
        if (!handler.isSelected(index)) {
            val tabY = posY + index * TAB_HEIGHT
            RenderKit.renderTexture(matrices, TEXTURE, posX, tabY,
                side.u, 0, TAB_WIDTH, TAB_HEIGHT
            )
            itemRenderer.renderInGuiWithOverrides(icon, posX + side.iconOffset, tabY + 6)
            if (isOverTab(mouseX, mouseY)){
                handler.drawTooltip(matrices!!, mouseX, mouseY, index)
            }
        }
    }

    fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int) {
        if (handler.isSelected(index)) {
            val tabY = posY + index * TAB_HEIGHT
            RenderKit.renderTexture(matrices, TEXTURE, posX, tabY,
                side.u, 28, TAB_WIDTH, TAB_HEIGHT
            )
            itemRenderer.renderInGuiWithOverrides(icon, posX + side.iconOffset, tabY + 6)
            if (isOverTab(mouseX, mouseY)){
                handler.drawTooltip(matrices!!, mouseX, mouseY, index)
            }
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, button: Int): Boolean {
        if (button == 0 && isOverTab(mouseX, mouseY)){
            handler.getClickSound()?.let { playSound(it) }
            handler.onClick(index)
            return true
        }
        return false
    }

    fun isOverTab(mouseX: Int, mouseY: Int): Boolean {
        val tabX = posX + side.interactOffset
        val tabY = posY + index * TAB_HEIGHT
        return mouseX >= tabX && mouseX <= tabX + 28 && mouseY >= tabY && mouseY < tabY + TAB_HEIGHT
    }

    enum class Side(val u: Int, val iconOffset: Int, val interactOffset: Int, val windowX: Int) {
        LEFT(32, 9, 0, -28),
        RIGHT(0, 7, 3, 242);
    }

    interface Handler {
        fun onClick(index: Int)
        fun isSelected(index: Int): Boolean
        fun getClickSound(): SoundEvent? = SoundEvents.ITEM_BOOK_PAGE_TURN
        fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int)
    }
}