package io.github.nbcss.wynnlib.timer

object TimerManager {
    private val timerMap: MutableMap<String, ITimer> = mutableMapOf()
    private var worldTime: Long = 0

    fun registerTimer(timer: ITimer) {
        synchronized(this) {
            if (timer.getKey() !in timerMap)
                timerMap[timer.getKey()] = timer
        }
    }

    fun getWorldTime(): Long {
        return worldTime
    }

    fun hasTimer(key: String): Boolean {
        return key in timerMap
    }

    fun updateWorldTime(time: Long) {
        synchronized(this) {
            worldTime = time
            val keys = timerMap.keys.toList()
            for (key in keys) {
                val timer = timerMap[key]!!
                timer.updateWorldTime(worldTime)
                if (timer.isExpired()){
                    timerMap.remove(key)
                }
            }
        }
    }

    fun onEvent(event: ITimer.ClearEvent) {
        synchronized(this) {
            val keys = timerMap.keys.toList()
            for (key in keys) {
                val timer = timerMap[key]!!
                if (timer.onClear(event)){
                    timerMap.remove(key)
                }
            }
        }
    }

    fun getSideTimers(): List<SideIndicator> {
        val timers: MutableList<SideIndicator> = mutableListOf()
        synchronized(this) {
            for (timer in timerMap.values) {
                timer.asSideIndicator()?.let { timers.add(it) }
            }
        }
        return timers
    }

    fun getIconTimers(): List<IconIndicator> {
        val timers: MutableList<IconIndicator> = mutableListOf()
        synchronized(this) {
            for (timer in timerMap.values) {
                timer.asIconIndicator()?.let { timers.add(it) }
            }
        }
        return timers
    }
}