package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange

abstract class RegularWearable(protected val parent: RegularEquipment, json: JsonObject)
    : Wearable, EquipmentCategory {
    private val health: Int
    private val elemDefence: MutableMap<Element, Int> = LinkedHashMap()
    init {
        health = if(json.has("health")) json.get("health").asInt else 0
        Element.values().forEach{elemDefence[it] = json.get(it.defenceName).asInt }
    }

    override fun getHealth(): IRange = SimpleIRange(health, health)

    override fun getElementDefence(elem: Element): Int {
        return elemDefence.getOrDefault(elem, 0)
    }
}