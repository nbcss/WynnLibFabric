package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.*
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.utils.ItemFactory.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.ItemFactory.fromLegacyId
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.asRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularWeapon(private val parent: RegularEquipment, json: JsonObject)
    : Weapon, EquipmentCategory {
    private val elemDamage: MutableMap<Element, IRange> = LinkedHashMap()
    private val type: EquipmentType
    private val damage: IRange
    private val atkSpeed: AttackSpeed
    private val texture: ItemStack

    init {
        type = EquipmentType.getEquipmentType(json.get("type").asString)
        damage = asRange(json.get("damage").asString)
        atkSpeed = AttackSpeed.fromName(json.get("attackSpeed").asString)
        Element.values().forEach{elemDamage[it] = asRange(json.get(it.damageName).asString)}
        texture = if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            val item = fromLegacyId(materials[0].toInt(), meta)
            if (item != ERROR_ITEM) item else type.getIcon()
        } else {
            getType().getIcon()
        }
    }

    override fun getDamage(): IRange = damage

    override fun getElementDamage(elem: Element): IRange {
        return elemDamage.getOrDefault(elem, IRange.ZERO)
    }

    override fun getAttackSpeed(): AttackSpeed = atkSpeed

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack {
        return texture
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(parent.getDisplayText())
        tooltip.addAll(getDamageTooltip())
        tooltip.add(LiteralText.EMPTY)
        addRequirements(parent, tooltip)
        tooltip.add(LiteralText.EMPTY)
        //append empty line if success add any id into the tooltip
        if (addIdentifications(parent , tooltip, parent.getClassReq()))
            tooltip.add(LiteralText.EMPTY)
        addPowderSlots(parent, tooltip)
        addItemSuffix(parent, tooltip)
        addRestriction(parent, tooltip)
        return tooltip
    }
}