package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Version
import java.util.function.Consumer

abstract class Registry<T: Keyed> {
    protected val itemMap: MutableMap<String, T> = LinkedHashMap()
    private var version: Version? = null

    protected abstract fun read(data: JsonObject): T?

    fun reload(json: JsonObject){
        val ver = Version(json["version"].asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        val array = json["data"].asJsonArray
        reload(array)
        version = ver
    }

    open fun reload(array: JsonArray){
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