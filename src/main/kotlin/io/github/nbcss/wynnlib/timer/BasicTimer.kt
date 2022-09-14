package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.render.RenderKit.renderDefaultOutlineText
import io.github.nbcss.wynnlib.utils.formatTimer
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class BasicTimer(entry: StatusEntry,
                      startTime: Long):
    AbstractFooterEntryTimer(entry, startTime), SideIndicator {
    private var maxDuration: Double? = entry.duration?.toDouble()

    open fun getDisplayText(): Text {
        return LiteralText(entry.icon).append(" ")
            .append(LiteralText(entry.name).formatted(Formatting.GRAY))
    }

    override fun getDuration(): Double? {
        if (maxDuration == null)
            return null
        return super.getDuration()
    }

    override fun updateEntry(currentEntry: StatusEntry) {
        if (currentEntry.duration != null) {
            if (timeTracker.updateRemainTime(currentEntry.duration)){
                maxDuration = entry.duration?.toDouble()?.plus(1)
            }
        }
    }

    override fun getFullDuration(): Double? = maxDuration

    override fun getKey(): String = entry.icon + "@" + entry.name

    override fun asSideIndicator(): SideIndicator = this

    override fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int) {
        val text = LiteralText("")
        val duration: Double? = getDuration()
        if (duration != null) {
            var color = Formatting.GREEN
            if (duration < 10) {
                color = Formatting.RED
            } else if (duration < 30) {
                color = Formatting.GOLD
            }
            text.append(LiteralText(formatTimer((duration * 1000).toLong())).formatted(color))
                .append(" ")
        }
        text.append(getDisplayText())
        renderDefaultOutlineText(matrices, text, posX.toFloat(), posY.toFloat())
    }
}