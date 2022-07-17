package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.*
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularArmour(parent: RegularEquipment, json: JsonObject)
    : RegularWearable(parent, json) {
    private val type: EquipmentType
    private val texture: ItemStack

    init {
        type = EquipmentType.getEquipmentType(json.get("type").asString)
        if (json.has("skin")) {
            val skin: String = json.get("skin").asString
            texture = ItemFactory.fromSkin(skin)
        } else if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            texture = ItemFactory.fromLegacyId(materials[0].toInt(), meta)
        } else if (json.has("armorType")) {
            val material: String = json.get("armorType").asString
            val copy = type.getTexture(material)
            val tag: NbtCompound = copy.orCreateNbt
            if (json.has("armorColor")) {
                val color: Int = asColor(json.get("armorColor").asString)
                if (color != -1){
                    if (material.equals("leather", ignoreCase = true)) {
                        val display = if (tag.contains("display")) tag.getCompound("display") else NbtCompound()
                        display.putInt("color", color)
                        tag.put("display", display)
                    }
                }
            }
            tag.putBoolean("Unbreakable", true)
            copy.nbt = tag
            texture = copy
        } else {
            texture = ItemFactory.ERROR_ITEM
        }
    }

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack = texture

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(parent.getDisplayText())
        tooltip.add(LiteralText.EMPTY)
        val defense = getDefenseTooltip()
        if (defense.isNotEmpty()){
            tooltip.addAll(defense)
            tooltip.add(LiteralText.EMPTY)
        }
        addRequirements(parent, tooltip)
        tooltip.add(LiteralText.EMPTY)
        //append empty line if success add any id into the tooltip
        if (addIdentifications(parent , tooltip))
            tooltip.add(LiteralText.EMPTY)
        addPowderSlots(parent, tooltip)
        addItemSuffix(parent, tooltip)
        addRestriction(parent, tooltip)
        return tooltip
    }
}