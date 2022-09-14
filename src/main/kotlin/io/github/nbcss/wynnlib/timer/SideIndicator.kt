package io.github.nbcss.wynnlib.timer

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack

interface SideIndicator: Comparable<SideIndicator> {
    fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int)
    fun getDuration(): Double?
    override fun compareTo(other: SideIndicator): Int {
        val duration1 = getDuration()
        val duration2 = other.getDuration()
        return if (duration1 != null && duration2 != null) {
            duration1.compareTo(duration2)
        }else if (duration1 != null) {
            -1
        }else if (duration2 != null) {
            1
        }else{
            0
        }
    }
}