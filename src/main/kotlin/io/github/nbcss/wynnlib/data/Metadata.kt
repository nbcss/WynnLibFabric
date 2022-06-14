package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Version
import java.util.function.Consumer

object Metadata {
    private val tierMap: MutableMap<String, Tier> = LinkedHashMap()
    private val idMap: MutableMap<String, Identification> = LinkedHashMap()
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
        version = ver
    }

    fun asIdentification(name: String): Identification? {
        return idMap[name]
    }

    fun asTier(name: String): Tier? {
        return tierMap[name]
    }

    fun getIdentifications(): List<Identification> {
        return idMap.values.toList()
    }
}