package io.github.nbcss.wynnlib.timer

abstract class AbstractFooterEntryTimer(protected val entry: StatusEntry,
                                        startTime: Long): ITimer {
    private var expired: Boolean = false
    protected val timeTracker: TimeTracker = TimeTracker(startTime,
        if (entry.duration != null) entry.duration + 1 else 0)

    fun getSyncTime(): Long = timeTracker.getCurrentTime()

    override fun updateWorldTime(time: Long) {
        timeTracker.setWorldTime(time)
        val currentEntry = StatusEntry.getStatusEntry(getKey())
        if (currentEntry == null) {
            expired = true
        } else {
            updateEntry(currentEntry)
        }
    }

    override fun getDuration(): Double? {
        if (timeTracker.getExpectEndTime() <= 0L)
            return null
        return timeTracker.getDuration()
    }

    override fun isExpired(): Boolean = expired

    open fun updateEntry(currentEntry: StatusEntry) {
        if (currentEntry.duration != null) {
            timeTracker.updateRemainTime(currentEntry.duration)
            /*if (abs(lowerTime - endTime) >= 20 && abs(upperTime - endTime) >= 20) {
                endTime = upperTime
            }*/
        }
    }
}