package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Version
import java.io.InputStreamReader

abstract class Registry<T: Keyed> {
    protected val itemMap: MutableMap<String, T> = LinkedHashMap()
    private var version: Version? = null

    protected abstract fun read(data: JsonObject): T?

    protected open fun getFilename(): String? = null

    fun load() {
        getFilename()?.let {
            val reader = InputStreamReader(FileUtils.getResource(it)!!, "utf-8")
            reload(JsonParser.parseReader(reader).asJsonObject)
        }
    }

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