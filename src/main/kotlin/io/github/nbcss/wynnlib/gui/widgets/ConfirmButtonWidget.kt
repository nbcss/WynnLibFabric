package io.github.nbcss.wynnlib.gui.widgets

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import java.util.function.Consumer

class ConfirmButtonWidget(private val handler: Consumer<ConfirmButtonWidget>,
                          private val tooltipProvider: TooltipProvider? = null,
                          private val screen: TooltipScreen,
                          x: Int, y: Int):
    PressableWidget(x, y, 10, 10, LiteralText.EMPTY) {
    private val texture: Identifier = Identifier("wynnlib", "textures/gui/check_button.png")
    override fun appendNarrations(builder: NarrationMessageBuilder?) {
        appendDefaultNarrations(builder)
    }

    override fun onPress() {
        handler.accept(this)
    }

    override fun renderButton(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        matrices!!.push()
        RenderSystem.enableDepthTest()
        val v = if (isHovered) 10 else 0
        RenderKit.renderTexture(matrices, texture, x, y, 0, v, 10, 10, 10, 20)
        matrices.pop()
        if(isHovered && tooltipProvider != null) {
            val tooltip = tooltipProvider.getTooltip()
            screen.drawTooltip(matrices, tooltip, mouseX, mouseY)
        }
    }
}