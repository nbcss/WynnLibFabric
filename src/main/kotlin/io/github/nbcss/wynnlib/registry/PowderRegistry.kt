package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Powder

object PowderRegistry: Registry<Powder>() {
    private const val RESOURCE = "assets/wynnlib/data/Powders.json"
    private val nameMap: MutableMap<String, Powder> = LinkedHashMap()

    fun fromName(name: String): Powder? {
        return nameMap[name]
    }

    override fun reload(array: JsonArray) {
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: Powder) {
        super.put(item)
        nameMap[item.getDisplayName()] = item
    }

    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Powder? = try {
        Powder(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}