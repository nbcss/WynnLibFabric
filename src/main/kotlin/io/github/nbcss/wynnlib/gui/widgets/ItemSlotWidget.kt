package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack

class ItemSlotWidget<T: BaseItem>(x: Int, y: Int,
                                  private val size: Int,
                                  private val margin: Int,
                                  private var item: T?,
                                  private val screen: TooltipScreen
                                  ): PressableWidget(x, y, size, size, null) {
    private val client: MinecraftClient = MinecraftClient.getInstance()
    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    fun getItem(): T? = item

    fun setItem(item: T?){
        this.item = item
    }

    override fun onPress() {

    }

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if(item == null)
            return
        matrices!!.push()
        RenderSystem.enableDepthTest()
        val color = item!!.getRarityColor().withAlpha(if (isHovered) 0xDD else 0x75).code()
        DrawableHelper.fill(matrices, x + margin, y + margin,
            x + size - margin, y + size - margin, color)
        val itemX: Int = x + width / 2 - 8
        val itemY: Int = y + height / 2 - 8
        val icon = item!!.getIcon()
        val text = item!!.getIconText()
        client.itemRenderer.renderInGuiWithOverrides(icon, itemX, itemY)
        client.itemRenderer.renderGuiItemOverlay(client.textRenderer, icon, itemX, itemY, text)
        if(isHovered){
            screen.drawTooltip(matrices, item!!.getTooltip(), mouseX, mouseY)
        }
        matrices.pop()
    }
}