package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Version
import java.util.function.Consumer

object Metadata {
    private val tierMap: MutableMap<String, Tier> = LinkedHashMap()
    private val idMap: MutableMap<String, Identification> = LinkedHashMap()
    private val atkSpeedMap: MutableMap<String, AttackSpeed> = LinkedHashMap()
    private var version: Version? = null

    fun reload(json: JsonObject) {
        val ver = Version(json.get("version").asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver) return
        //reload tiers
        tierMap.clear()
        json.get("tiers").asJsonArray.forEach(Consumer {
            val tier = Tier(it.asJsonObject)
            tierMap[tier.getKey()] = tier
        })
        //reload identifications
        idMap.clear()
        json.get("identifications").asJsonArray.forEach(Consumer {
            val id = Identification(it.asJsonObject)
            idMap[id.getKey()] = id
        })
        //reload attack speed
        atkSpeedMap.clear()
        json.get("attackSpeeds").asJsonArray.forEach(Consumer {
            val atkSpeed = AttackSpeed(it.asJsonObject)
            atkSpeedMap[atkSpeed.getKey()] = atkSpeed
        })
        version = ver
    }

    fun asIdentification(name: String): Identification? {
        return idMap[name]
    }

    fun asTier(name: String): Tier? {
        return tierMap[name]
    }

    fun asAttackSpeed(name: String): AttackSpeed? {
        return atkSpeedMap[name]
    }

    fun getIdentifications(): List<Identification> {
        return idMap.values.toList()
    }
}