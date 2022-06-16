package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.Wearable
import io.github.nbcss.wynnlib.utils.IRange

abstract class RegularWearable(protected val parent: RegularEquipment, json: JsonObject)
    : Wearable, EquipmentContainer {
    private val health: Int
    private val elemDefence: MutableMap<Element, Int> = LinkedHashMap()
    init {
        health = if(json.has("health")) json.get("health").asInt else 0
        Element.values().forEach{elemDefence[it] = json.get(it.defenceName).asInt }
    }

    override fun getHealth(): IRange = IRange(health, health)

    override fun getElementDefense(elem: Element): Int {
        return elemDefence.getOrDefault(elem, 0)
    }
}