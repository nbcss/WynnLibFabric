package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.utils.Keyed

interface ITimer: Keyed {
    fun isExpired(): Boolean
    fun updateWorldTime(time: Long)
    fun getDuration(): Double?
    fun getFullDuration(): Double?
    fun asSideTimer(): SideTimer? = null
    fun asIconTimer(): IconTimer? = null
    fun onClear(event: ClearEvent): Boolean = true

    companion object {

        fun fromEntry(entry: StatusEntry): ITimer {
            AbilityTimer.matches(entry, TimerManager.getWorldTime())?.let { return it }
            return EffectTimer(entry, TimerManager.getWorldTime())
        }
    }

    enum class ClearEvent {
        LEVEL_RESET
    }
}