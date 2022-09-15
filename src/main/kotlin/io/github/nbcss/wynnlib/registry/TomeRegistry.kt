package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.misc.Tome

object TomeRegistry : Registry<Tome>() {
    private const val RESOURCE = "assets/wynnlib/data/Tomes.json"
    private val nameMap: MutableMap<String, Tome> = LinkedHashMap()

    fun fromName(name: String): Tome? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray) {
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: Tome) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): Tome? = try {
        Tome(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}