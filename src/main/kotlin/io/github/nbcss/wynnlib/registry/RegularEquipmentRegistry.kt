package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.regular.RegularEquipment
import io.github.nbcss.wynnlib.utils.Registry

object RegularEquipmentRegistry: Registry<RegularEquipment>() {
    override fun read(data: JsonObject): RegularEquipment? = try {
        RegularEquipment(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}