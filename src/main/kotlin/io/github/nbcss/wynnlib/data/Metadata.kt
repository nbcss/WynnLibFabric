package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Version
import java.util.*
import java.util.function.Consumer
import kotlin.collections.LinkedHashMap

object Metadata {
    private val tierMap: MutableMap<String, Tier> = LinkedHashMap()
    private val idMap: MutableMap<String, Identification> = LinkedHashMap()
    private val atkSpeedMap: MutableMap<String, AttackSpeed> = LinkedHashMap()
    private val elementMap: MutableMap<String, Element> = LinkedHashMap()
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
        //reload tiers
        tierMap.clear()
        json.get("tiers").asJsonArray.forEach(Consumer {
            val tier = Tier(it.asJsonObject)
            tierMap[tier.getKey().lowercase(Locale.getDefault())] = tier
        })
        //reload identifications
        idMap.clear()
        json.get("identifications").asJsonArray.forEach(Consumer {
            val id = Identification(it.asJsonObject)
            idMap[id.getKey().lowercase(Locale.getDefault())] = id
        })
        //reload attack speed
        atkSpeedMap.clear()
        json.get("attackSpeeds").asJsonArray.forEach(Consumer {
            val atkSpeed = AttackSpeed(it.asJsonObject)
            atkSpeedMap[atkSpeed.getKey().lowercase(Locale.getDefault())] = atkSpeed
        })
        //reload elements
        elementMap.clear()
        json.get("elements").asJsonArray.forEach(Consumer {
            val elem = Element(it.asJsonObject)
            elementMap[elem.getKey().lowercase(Locale.getDefault())] = elem
        })
        version = ver
    }

    fun asEquipmentType(name: String): EquipmentType? {
        return equipmentTypeMap[name.lowercase(Locale.getDefault())]
    }

    fun asIdentification(name: String): Identification? {
        return idMap[name.lowercase(Locale.getDefault())]
    }

    fun asTier(name: String): Tier? {
        return tierMap[name.lowercase(Locale.getDefault())]
    }

    fun asAttackSpeed(name: String): AttackSpeed? {
        return atkSpeedMap[name.lowercase(Locale.getDefault())]
    }

    fun getIdentifications(): List<Identification> {
        return idMap.values.toList()
    }

    fun getElements(): List<Element> {
        return elementMap.values.toList()
    }
}