package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.regular.RegularTome

object TomeRegistry : Registry<RegularTome>() {
    private const val RESOURCE = "assets/wynnlib/data/Tomes.json"
    private val nameMap: MutableMap<String, RegularTome> = LinkedHashMap()

    fun fromName(name: String): RegularTome? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray) {
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: RegularTome) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): RegularTome? = try {
        RegularTome(data)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}