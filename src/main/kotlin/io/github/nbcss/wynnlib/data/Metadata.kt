package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Version
import java.util.function.Consumer

object Metadata {
    private val tiers: MutableMap<String, Tier> = LinkedHashMap()
    private val ids: MutableMap<String, Identification> = LinkedHashMap()
    private var version: Version? = null

    fun reload(json: JsonObject) {
        val ver = Version(json.get("version").asString)
        //skip reload if currently have newer version
        if(version != null && version!! > ver){
            return
        }
        //reload tiers
        tiers.clear()
        json.get("tiers").asJsonArray.forEach(Consumer {
            val tier = Tier(it.asJsonObject)
            tiers[tier.getKey()] = tier
        })
        //reload identifications
        ids.clear()
        json.get("identifications").asJsonArray.forEach(Consumer {
            val id = Identification(it.asJsonObject)
            ids[id.getKey()] = id
        })
        version = ver
    }

    fun asIdentification(name: String): Identification? {
        return ids[name]
    }

    fun asTier(name: String): Tier {
        return tiers.getOrDefault(name, Tier(name))
    }

    fun getIdentifications(): List<Identification> {
        return ids.values.toList()
    }
}