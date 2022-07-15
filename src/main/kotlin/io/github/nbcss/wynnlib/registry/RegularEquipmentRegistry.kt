package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment

object RegularEquipmentRegistry: Registry<RegularEquipment>() {
    private const val RESOURCE = "assets/wynnlib/data/Equipments.json"
    private val nameMap: MutableMap<String, RegularEquipment> = LinkedHashMap()

    fun fromName(name: String): RegularEquipment? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray){
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: RegularEquipment) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): RegularEquipment? = try {
        RegularEquipment(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}