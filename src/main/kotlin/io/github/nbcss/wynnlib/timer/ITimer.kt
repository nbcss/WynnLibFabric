package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.utils.Keyed

interface ITimer: Keyed {
    fun isExpired(): Boolean
    fun updateWorldTime(time: Long)
    fun getDuration(): Double?
    fun getFullDuration(): Double?
    fun asSideTimer(): SideTimer? = null

    companion object {

        fun fromEntry(entry: FooterEntry): ITimer {
            return EffectTimer(entry, TimerManager.getWorldTime())
        }
    }
}