package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack

class ItemSlotWidget<T: BaseItem>(x: Int, y: Int, private var item: T?):
    PressableWidget(x, y, 24, 24, null) {
    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    fun setItem(item: T?){
        this.item = item
    }

    override fun onPress() {
        if(item != null)
            println("pressed" + item!!.getDisplayName())
    }

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        if(item == null)
            return
        val itemX: Int = x + width / 2 - 8
        val itemY: Int = y + height / 2 - 8
        MinecraftClient.getInstance().itemRenderer.renderGuiItemIcon(item!!.getIcon(), itemX, itemY)
    }
}