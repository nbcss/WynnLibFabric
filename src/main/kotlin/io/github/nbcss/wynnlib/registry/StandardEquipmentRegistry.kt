package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.standard.StandardEquipment
import io.github.nbcss.wynnlib.utils.Registry

class StandardEquipmentRegistry: Registry<StandardEquipment>() {
    override fun read(data: JsonObject): StandardEquipment? = try {
        StandardEquipment(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}