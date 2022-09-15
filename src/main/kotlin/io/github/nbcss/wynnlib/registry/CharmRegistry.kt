package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.regular.RegularCharm

object CharmRegistry : Registry<RegularCharm>() {
    private const val RESOURCE = "assets/wynnlib/data/Charms.json"
    private val nameMap: MutableMap<String, RegularCharm> = LinkedHashMap()

    fun fromName(name: String): RegularCharm? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray) {
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: RegularCharm) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): RegularCharm? = try {
        RegularCharm(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}