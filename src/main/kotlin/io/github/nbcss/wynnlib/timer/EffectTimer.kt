package io.github.nbcss.wynnlib.timer

import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.max

class EffectTimer(private val entry: FooterEntry,
                  startTime: Long): SideTimer {
    private var expired: Boolean = false
    private var currentTime: Long = startTime
    private val endTime: Long = startTime + (entry.duration?.plus(1)?.times(20) ?: 0).toLong()

    override fun getDisplayText(): Text {
        return LiteralText(entry.icon).append(" ")
            .append(LiteralText(entry.name).formatted(Formatting.GRAY))
    }

    override fun isExpired(): Boolean = expired

    override fun updateWorldTime(time: Long) {
        currentTime = time
        if (currentTime > endTime)
            expired = true
    }

    override fun getDuration(): Double? {
        if (entry.duration == null)
            return null
        val time = (endTime - currentTime) / 20.0
        return max(0.0, time)
    }

    override fun getFullDuration(): Double? {
        return entry.duration?.toDouble()
    }

    override fun getKey(): String = entry.name

    override fun asSideTimer(): SideTimer = this
}