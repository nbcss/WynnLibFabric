package io.github.nbcss.wynnlib.timer

import kotlin.math.max

class TimeTracker(private var currentTime: Long,
                  duration: Int = -1) {
    private var endTime: Long = if (duration > 0) toEndTime(currentTime, duration) else 0

    private fun toEndTime(time: Long, duration: Int): Long {
        return time + duration.times(20).minus(1).toLong()
    }

    fun getCurrentTime(): Long = currentTime

    fun getExpectEndTime(): Long = endTime

    fun setWorldTime(time: Long) {
        currentTime = time
    }

    fun updateRemainTime(duration: Int): Boolean {
        val upperTime = toEndTime(currentTime, duration + 1)
        val lowerTime = toEndTime(currentTime, duration)
        if (endTime - upperTime >= 10 || lowerTime - endTime >= 10) {
            endTime = upperTime
            return true
        }
        return false
    }

    fun getDuration(): Double {
        val time = (endTime - currentTime) / 20.0
        return max(0.0, time)
    }
}