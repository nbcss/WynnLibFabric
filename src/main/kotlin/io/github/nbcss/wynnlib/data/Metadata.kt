package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Version
import java.util.*
import java.util.function.Consumer
import kotlin.collections.LinkedHashMap

object Metadata {
    private val idMap: MutableMap<String, Identification> = LinkedHashMap()
    private val equipmentTypeMap: MutableMap<String, EquipmentType> = LinkedHashMap()
    private var version: Version? = null

    fun reload(json: JsonObject) {
        val ver = Version(json.get("version").asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        //reload equipment types
        equipmentTypeMap.clear()
        json.get("equipmentTypes").asJsonArray.forEach(Consumer {
            val type = EquipmentType(it.asJsonObject)
            equipmentTypeMap[type.getKey().lowercase(Locale.getDefault())] = type
        })
        //reload identifications
        idMap.clear()
        json.get("identifications").asJsonArray.forEach(Consumer {
            val id = Identification(it.asJsonObject)
            idMap[id.getKey().lowercase(Locale.getDefault())] = id
        })
        version = ver
    }

    fun asEquipmentType(name: String): EquipmentType? {
        return equipmentTypeMap[name.lowercase(Locale.getDefault())]
    }

    fun asIdentification(name: String): Identification? {
        return idMap[name.lowercase(Locale.getDefault())]
    }

    fun getIdentifications(): List<Identification> {
        return idMap.values.toList()
    }
}