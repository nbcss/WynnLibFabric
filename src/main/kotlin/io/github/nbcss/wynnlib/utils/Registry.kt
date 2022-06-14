package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.function.Consumer

abstract class Registry<T: Keyed> {
    private val values: MutableMap<String, T> = LinkedHashMap()
    private var version: Version? = null

    protected abstract fun read(data: JsonObject): T?

    fun reload(array: JsonArray, ver: Version){
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        reload(array)
        version = ver
    }

    fun reload(array: JsonArray){
        values.clear()
        array.forEach(Consumer {
            val item = read(it.asJsonObject)
            if (item!= null){
                values[item.getKey()] = item
            }
        })
    }

    fun put(item: T) {
        values[item.getKey()] = item
    }

    fun get(key: String): T? {
        return values[key]
    }
}