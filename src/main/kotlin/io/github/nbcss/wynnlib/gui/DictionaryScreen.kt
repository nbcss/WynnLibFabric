package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.utils.BaseItem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class DictionaryScreen<T: BaseItem>(title: Text) : HandbookTabScreen(title) {
    private val slots = Identifier("wynnlib", "textures/gui/dictionary_slots.png")
    protected val items: MutableList<T> = ArrayList()
    protected abstract fun fetchItems(): Collection<T>

    override fun init() {
        super.init()
        items.clear()
        items.addAll(fetchItems())
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, slots)
        val x: Int = (width - this.backgroundWidth) / 2
        val y: Int = (height - this.backgroundHeight) / 2
        this.drawTexture(matrices, x + 11, y + 16, 0, 0, this.backgroundWidth, this.backgroundHeight)
    }
}