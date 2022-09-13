package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Material

object MaterialRegistry: Registry<Material>() {
    private const val RESOURCE = "assets/wynnlib/data/Materials.json"

    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Material? = try {
        Material(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}