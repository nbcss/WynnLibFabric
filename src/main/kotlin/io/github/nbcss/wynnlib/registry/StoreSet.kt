package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.Scheduler
import kotlin.concurrent.thread

class StoreSet<T>(private val name: String,
                  private val path: String,
                  private val convertor: DataConvertor<T>) {
    object StringConvertor: DataConvertor<String> {
        override fun read(data: JsonElement): String? {
            return data.asString
        }
        override fun write(item: String): JsonElement {
            return JsonPrimitive(item)
        }
    }
    private val items: MutableSet<T> = mutableSetOf()
    private var dirty: Boolean = false
    private var saving: Boolean = false

    fun load() {
        FileUtils.readFile(path)?.let {
            reload(it)
        }
        Scheduler.registerTask(name, 20){
            save()
        }
    }

    fun add(item: T) {
        items.add(item)
        markDirty()
    }

    fun has(item: T): Boolean {
        return item in items
    }

    fun remove(item: T): Boolean {
        if (items.remove(item)){
            markDirty()
            return true
        }
        return false
    }

    fun reload(data: JsonObject) {
        items.clear()
        data["data"].asJsonArray.forEach{
            val item = convertor.read(it)
            if (item!= null){
                items.add(item)
            }
        }
    }

    private fun markDirty() {
        dirty = true
    }

    fun save() {
        if (dirty && !saving) {
            saving = true
            thread(isDaemon = true) {
                val array = JsonArray()
                for (item in items) {
                    array.add(convertor.write(item))
                }
                val data = JsonObject()
                data.add("data", array)
                FileUtils.writeFile(path, data)
                dirty = false
                saving = false
            }
        }
    }

    interface DataConvertor<T> {
        fun read(data: JsonElement): T?
        fun write(item: T): JsonElement
    }
}