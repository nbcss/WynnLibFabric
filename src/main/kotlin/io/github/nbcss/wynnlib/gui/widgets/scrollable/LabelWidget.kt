package io.github.nbcss.wynnlib.gui.widgets.scrollable

import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import io.github.nbcss.wynnlib.utils.Color
import java.util.function.Supplier

class LabelWidget(posX: Int, posY: Int,
                  private val textProvider: Supplier<Text?>,
                  private val colorProvider: Supplier<Color?>? = null,
                  private val mode: Mode = Mode.SHADOW): BaseScrollableWidget(posX, posY) {
    companion object {
        private val renderer = MinecraftClient.getInstance().textRenderer
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        textProvider.get()?.let {
            val color = colorProvider?.get() ?: Color.WHITE
            when (mode) {
                Mode.PLAIN -> {
                    renderer.draw(matrices, it, getX().toFloat(), getY().toFloat(), color.code())
                }
                Mode.SHADOW -> {
                    renderer.drawWithShadow(matrices, it, getX().toFloat(), getY().toFloat(), color.code())
                }
                Mode.OUTLINE -> {
                    RenderKit.renderOutlineText(matrices, it, getX().toFloat(), getY().toFloat(), color)
                }
            }
        }
    }

    enum class Mode {
        PLAIN,
        SHADOW,
        OUTLINE
    }
}