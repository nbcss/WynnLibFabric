package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack

class ItemSlotWidget<T: BaseItem>(x: Int, y: Int,
                                  private var item: T?,
                                  private val screen: TooltipScreen):
    PressableWidget(x, y, 22, 22, null) {
    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    fun setItem(item: T?){
        this.item = item
    }

    override fun onPress() {

    }

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if(item == null)
            return
        matrices!!.push()
        val color = item!!.getRarityColor() + ((if (isHovered) 0xDD else 0x75) shl 24)
        DrawableHelper.fill(matrices, x, y, x + 22, y + 22, color)
        val itemX: Int = x + width / 2 - 8
        val itemY: Int = y + height / 2 - 8
        MinecraftClient.getInstance().itemRenderer.renderGuiItemIcon(item!!.getIcon(), itemX, itemY)
        if(isHovered){
            screen.drawTooltip(matrices, item!!.getTooltip(), mouseX, mouseY)
        }
        matrices.pop()
    }
}