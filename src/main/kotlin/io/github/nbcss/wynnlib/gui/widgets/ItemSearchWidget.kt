package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.LiteralText

class ItemSearchWidget(textRenderer: TextRenderer?, x: Int, y: Int, width: Int, height: Int) :
    TextFieldWidget(textRenderer, x, y, width, height, LiteralText.EMPTY) {

    fun validate(item: BaseItem): Boolean {
        val name = item.getDisplayName()
        if (name.contains(text, ignoreCase = true))
            return true
        return name.contains(text, ignoreCase = true)
    }

    public override fun setFocused(focused: Boolean) {
        super.setFocused(focused)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 1 && isMouseOver(mouseX, mouseY)){
            text = ""
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
}