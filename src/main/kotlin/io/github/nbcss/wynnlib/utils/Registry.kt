package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.function.Consumer

abstract class Registry<T: Keyed> {
    private val itemMap: MutableMap<String, T> = LinkedHashMap()
    private var version: Version? = null

    protected abstract fun read(data: JsonObject): T?

    fun reload(json: JsonObject){
        val ver = Version(json.get("version").asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        val array = json.get("data").asJsonArray
        reload(array)
        version = ver
    }

    private fun reload(array: JsonArray){
        itemMap.clear()
        array.forEach(Consumer {
            val item = read(it.asJsonObject)
            if (item!= null){
                put(item)
            }
        })
    }

    fun put(item: T) {
        itemMap[item.getKey()] = item
    }

    fun get(key: String): T? {
        return itemMap[key]
    }

    fun getAll(): Collection<T> {
        return itemMap.values
    }
}