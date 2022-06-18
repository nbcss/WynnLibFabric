package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.equipments.EquipmentContainer
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.lang.Translatable.Companion.from
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

abstract class RegularWearable(protected val parent: RegularEquipment, json: JsonObject)
    : Wearable, EquipmentContainer {
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

    protected fun addDefenseTooltip(tooltip: MutableList<Text>): Boolean {
        val lastSize: Int = tooltip.size
        if (health != 0) {
            val text = LiteralText(": " + signed(health)).formatted(Formatting.DARK_RED)
            val prefix = from("wynnlib.tooltip.health").translate().formatted(Formatting.DARK_RED)
            tooltip.add(prefix.append(text))
        }
        Element.values().forEach {
            val value: Int = getElementDefence(it)
            if (value != 0) {
                val text = LiteralText(": " + signed(value)).formatted(Formatting.GRAY)
                val prefix = it.translate("tooltip.defence").formatted(Formatting.GRAY)
                tooltip.add(prefix.append(text))
            }
        }
        return tooltip.size > lastSize
    }
}