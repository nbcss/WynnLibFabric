package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Scheduler
import kotlin.concurrent.thread

abstract class SavingStorage<T: Keyed>: Storage<T>(), Keyed {
    private var dirty: Boolean = false
    private var saving: Boolean = false

    abstract fun getData(item: T): JsonObject
    abstract fun getSavePath(): String

    override fun load() {
        FileUtils.readFile(getSavePath())?.let {
            reload(it)
        }
        Scheduler.registerTask("SAVE_${getKey()}", 20){
            save()
        }
    }

    override fun put(item: T) {
        super.put(item)
        markDirty()
    }

    override fun remove(key: String): Boolean {
        if (super.remove(key)) {
            markDirty()
            return true
        }
        return false
    }

    override fun reload(array: JsonArray) {
        super.reload(array)
        dirty = false
    }

    fun markDirty() {
        dirty = true
    }

    fun save() {
        if (dirty && !saving) {
            saving = true
            thread(isDaemon = true) {
                val array = JsonArray()
                for (item in itemMap.values) {
                    array.add(getData(item))
                }
                val data = JsonObject()
                data.add("data", array)
                FileUtils.writeFile(getSavePath(), data)
                dirty = false
                saving = false
            }
        }
    }
}