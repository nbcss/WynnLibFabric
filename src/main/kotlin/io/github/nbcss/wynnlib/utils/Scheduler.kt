package io.github.nbcss.wynnlib.utils

object Scheduler {
    private val taskMap: MutableMap<String, TaskData> = mutableMapOf()

    fun tick() {
        for (entry in taskMap.entries) {
            val nextTask = entry.value.next()
            taskMap[entry.key] = nextTask
        }
    }

    fun registerTask(key: String, interval: Int, task: () -> Unit) {
        taskMap[key] = TaskData(0, interval, task)
    }

    private data class TaskData(val tick: Int,
                                val interval: Int,
                                val task: () -> Unit){
        fun next(): TaskData {
            val nextTick = tick + 1
            if (nextTick > interval) {
                task.invoke()
                return TaskData(0, interval, task)
            }
            return TaskData(nextTick, interval, task)
        }
    }
}