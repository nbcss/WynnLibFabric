package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.data.Metadata.asEquipmentType
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.utils.asRange
import io.github.nbcss.wynnlib.utils.getItemById
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularWeapon(private val parent: RegularEquipment, json: JsonObject)
    : Weapon, EquipmentContainer {
    private val elemDamage: MutableMap<Element, IntRange> = LinkedHashMap()
    private val type: EquipmentType
    private val damage: IntRange
    private val atkSpeed: AttackSpeed
    private val texture: ItemStack

    init {
        type = asEquipmentType(json.get("type").asString)!!
        damage = asRange(json.get("damage").asString)
        atkSpeed = Metadata.asAttackSpeed(json.get("attackSpeed").asString)!!
        Metadata.getElements().forEach{elemDamage[it] = asRange(json.get(it.damageName).asString)}
        texture = if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            getItemById(materials[0].toInt(), meta)
        } else {
            getType().getIcon()
        }
    }

    override fun getDamage(): IntRange = damage

    override fun getElementDamage(elem: Element): IntRange {
        return elemDamage.getOrDefault(elem, IntRange(0, 0))
    }

    override fun getAttackSpeed(): AttackSpeed = atkSpeed

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack {
        return texture
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(LiteralText(parent.getDisplayName()))
        return tooltip
    }
}