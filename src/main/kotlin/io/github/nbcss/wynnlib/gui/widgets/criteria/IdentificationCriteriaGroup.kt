package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.IdentificationGroup
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.IdentificationHolder
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

class IdentificationCriteriaGroup<T>(private val group: IdentificationGroup,
                                     private val ids: List<Identification>) : CriteriaGroup<T>
        where T : BaseItem, T: IdentificationHolder {
    companion object {
        fun <T> of(): List<IdentificationCriteriaGroup<T>> where T : BaseItem, T: IdentificationHolder {
            return IdentificationGroup.values().map { IdentificationCriteriaGroup(it, Identification.fromGroup(it)) }
        }
    }
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float) {
        var itemY = posY.toFloat()
        for (identification in ids) {
            val text = identification.translate(Formatting.GRAY, null)
            if (identification.suffix == "%") {
                text.append(LiteralText(" %").formatted(Formatting.GOLD))
            }
            /*MinecraftClient.getInstance().textRenderer
                .drawWithShadow(matrices, text, posX.toFloat() + 2.0f, itemY + 5.0f, 0xFFFFFF)*/
            RenderKit.renderOutlineText(matrices, text, posX.toFloat() + 2.0f, itemY + 5.0f)
            itemY += 20.0f
        }
    }

    override fun reload(memory: CriteriaMemory<T>) {
        println("reload $group")
    }

    override fun getHeight(): Int {
        return ids.size * 20
    }
}