package io.github.nbcss.wynnlib.timer

import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.abs

class EffectTimer(entry: StatusEntry,
                  startTime: Long):
    AbstractFooterEntryTimer(entry, startTime), SideTimer {
    private var maxDuration: Double? = entry.duration?.toDouble()

    override fun getDisplayText(): Text {
        return LiteralText(entry.icon).append(" ")
            .append(LiteralText(entry.name).formatted(Formatting.GRAY))
    }

    override fun getDuration(): Double? {
        if (maxDuration == null)
            return null
        return super.getDuration()
    }

    override fun updateEntry(currentEntry: StatusEntry) {
        val currentTime = TimerManager.getWorldTime()
        if (currentEntry.duration != null) {
            val upperTime = toEndTime(currentTime, currentEntry.duration + 1)
            val lowerTime = toEndTime(currentTime, currentEntry.duration)
            if (abs(lowerTime - endTime) >= 20 && abs(upperTime - endTime) >= 20) {
                maxDuration = entry.duration?.toDouble()
                endTime = upperTime
            }
        }
    }

    override fun getFullDuration(): Double? = maxDuration

    override fun getKey(): String = entry.icon + "@" + entry.name

    override fun asSideTimer(): SideTimer = this
}