package io.github.nbcss.wynnlib.timer

import kotlin.math.abs
import kotlin.math.max

abstract class AbstractFooterEntryTimer(protected val entry: StatusEntry,
                                        startTime: Long): ITimer {
    private var expired: Boolean = false
    private var currentTime: Long = startTime
    protected var endTime: Long = if (entry.duration != null) toEndTime(startTime, entry.duration + 1) else 0

    protected fun toEndTime(time: Long, duration: Int): Long {
        return time + duration.times(20).minus(1).toLong()
    }

    override fun updateWorldTime(time: Long) {
        currentTime = time
        val currentEntry = StatusEntry.getStatusEntry(getKey())
        if (currentEntry == null) {
            expired = true
        } else {
            updateEntry(currentEntry)
        }
    }

    override fun getDuration(): Double? {
        val time = (endTime - currentTime) / 20.0
        return max(0.0, time)
    }

    override fun isExpired(): Boolean = expired

    open fun updateEntry(currentEntry: StatusEntry) {
        val currentTime = TimerManager.getWorldTime()
        if (currentEntry.duration != null) {
            val upperTime = toEndTime(currentTime, currentEntry.duration + 1)
            val lowerTime = toEndTime(currentTime, currentEntry.duration)
            if (endTime - upperTime >= 10) {
                endTime = upperTime
            }else if(lowerTime - endTime >= 10) {
                endTime = lowerTime
            }
            /*if (abs(lowerTime - endTime) >= 20 && abs(upperTime - endTime) >= 20) {
                endTime = upperTime
            }*/
        }
    }
}