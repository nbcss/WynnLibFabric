package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment

object RegularEquipmentRegistry: Registry<RegularEquipment>() {
    private const val RESOURCE = "assets/wynnlib/data/Equipments.json"

    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): RegularEquipment? = try {
        RegularEquipment(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}