package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Material

object MaterialRegistry : Registry<Material>() {
    private const val RESOURCE = "assets/wynnlib/data/Materials.json"
    private val itemNameMap: MutableMap<String, Material> = mutableMapOf()

    fun fromItemName(name: String): Material? {
        return itemNameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray) {
        itemNameMap.clear()
        super.reload(array)
    }

    override fun put(item: Material) {
        super.put(item)
        itemNameMap[item.getItemName()] = item
    }

    override fun read(data: JsonObject): Material? = try {
        Material(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}