package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.timer.indicators.TypedStatusTimer
import io.github.nbcss.wynnlib.utils.Keyed

interface ITimer: Keyed {
    fun isExpired(): Boolean
    fun updateWorldTime(time: Long)
    fun getDuration(): Double?
    fun getFullDuration(): Double?
    fun asSideIndicator(): SideIndicator? = null
    fun asIconIndicator(): IconIndicator? = null
    fun onClear(event: ClearEvent): Boolean = true

    companion object {

        fun fromEntry(entry: StatusEntry): ITimer {
            //println(StringEscapeUtils.escapeJava(entry.icon))
            val worldTime = TimerManager.getWorldTime()
            TypedStatusTimer.fromStatus(entry, worldTime)?.let { return it }
            return SimpleTimer(entry, worldTime)
        }
    }

    enum class ClearEvent {
        LEVEL_RESET
    }
}