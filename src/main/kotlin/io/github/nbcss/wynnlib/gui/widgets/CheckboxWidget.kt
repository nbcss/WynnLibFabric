package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text

class CheckboxWidget(private val posX: Int,
                     private val posY: Int,
                     private var checked: Boolean = true):
    ClickableWidget(-1000, -1000, SIZE, SIZE, fromBoolean(checked)) {
    companion object {
        const val SIZE = 20
        private fun fromBoolean(checked: Boolean): Text {
            return if (checked) Symbol.TICK.asText() else Symbol.CROSS.asText()
        }
    }

    init {
        visible = false
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
        message = fromBoolean(checked)
    }

    fun isChecked(): Boolean = checked

    fun updatePosition(x: Int, y: Int) {
        this.x = posX + x
        this.y = posY + y
        this.visible = true
    }

    override fun onClick(mouseX: Double, mouseY: Double) {
        setChecked(!isChecked())
    }

    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }
}