package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

abstract class Storage<T: Keyed> {
    protected val itemMap: MutableMap<String, T> = linkedMapOf()

    abstract fun load()

    protected abstract fun read(data: JsonObject): T?

    open fun reload(json: JsonObject){
        val array = json["data"].asJsonArray
        reload(array)
    }

    open fun reload(array: JsonArray){
        itemMap.clear()
        array.forEach{
            val item = read(it.asJsonObject)
            if (item!= null){
                put(item)
            }
        }
    }

    open fun put(item: T) {
        itemMap[item.getKey()] = item
    }

    fun get(key: String): T? {
        return itemMap[key]
    }

    fun getAll(): Collection<T> {
        return itemMap.values
    }
}