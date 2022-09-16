package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier

class SquareButton(private val texture: Identifier,
                   x: Int,
                   y: Int,
                   private val size: Int,
                   private val screen: TooltipScreen? = null,
                   private val tooltipProvider: TooltipProvider? = null,
                   private val pressSound: SoundEvent? = SoundEvents.UI_BUTTON_CLICK,
                   onPress: PressAction):
    ButtonWidget(x, y, size, size, LiteralText.EMPTY, onPress) {

    override fun playDownSound(soundManager: SoundManager?) {
        pressSound?.let {
            soundManager?.play(PositionedSoundInstance.master(it, 1.0f))
        }
    }

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val v = if (this.isHovered) size else 0
        RenderKit.renderTexture(matrices, texture, x, y, 0, v, size, size, size, size * 2)
        if (this.isHovered && screen != null && tooltipProvider != null) {
            screen.drawTooltip(matrices!!, tooltipProvider.getTooltip(), mouseX, mouseY)
        }
    }
}