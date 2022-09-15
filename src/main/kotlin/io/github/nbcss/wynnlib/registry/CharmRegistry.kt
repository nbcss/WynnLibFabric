package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.misc.Charm

object CharmRegistry : Registry<Charm>() {
    private const val RESOURCE = "assets/wynnlib/data/Charms.json"
    private val nameMap: MutableMap<String, Charm> = LinkedHashMap()

    fun fromName(name: String): Charm? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray) {
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: Charm) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): Charm? = try {
        Charm(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}