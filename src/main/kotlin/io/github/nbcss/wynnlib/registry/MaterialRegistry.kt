package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Material

object MaterialRegistry: Registry<Material>() {
    override fun read(data: JsonObject): Material? = try {
        Material(Material.Tier.STAR_1, data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}