package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Material

object MaterialRegistry: Registry<Material>() {
    private const val RESOURCE = "assets/wynnlib/data/Materials.json"
    private var tier: Material.Tier = Material.Tier.STAR_1

    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Material? = try {
        Material(tier, data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    override fun reload(array: JsonArray){
        itemMap.clear()
        array.forEach{
            Material.Tier.values().forEach { tier ->
                this.tier = tier
                val item = read(it.asJsonObject)
                if (item!= null){
                    put(item)
                }
            }
        }
    }
}