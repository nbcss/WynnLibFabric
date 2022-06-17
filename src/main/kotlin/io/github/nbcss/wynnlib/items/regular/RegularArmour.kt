package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.Wearable
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.Item
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
            texture = getSkullItem(skin)
        } else if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            texture = getItemById(materials[0].toInt(), meta)
        } else if (json.has("armorType")) {
            val material: String = json.get("armorType").asString
            val copy = type.getTexture(material).copy()
            val nbt: NbtCompound = copy.orCreateNbt
            val tag: NbtCompound = if (nbt.contains("tag")) nbt.getCompound("tag") else NbtCompound()
            if (json.has("armorColor")) {
                val color: Int = asColor(json.get("armorColor").asString)
                if (color != -1){
                    val id: Int = Item.getRawId(copy.item)
                    if (id in 298..301) {
                        val display = if (tag.contains("display")) tag.getCompound("display") else NbtCompound()
                        display.putInt("color", color)
                        tag.put("display", display)
                        nbt.put("tag", tag)
                        copy.writeNbt(nbt)
                    }
                }
            }
            tag.putBoolean("Unbreakable", true)
            nbt.put("tag", tag)
            copy.writeNbt(nbt)
            texture = copy
        } else {
            texture = ERROR_ITEM
        }
    }

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack = texture

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(parent.getDisplayText())
        tooltip.add(LiteralText(""))
        if (addDefenseTooltip(tooltip))
            tooltip.add(LiteralText(""))
        addRequirements(parent, tooltip)
        tooltip.add(LiteralText(""))
        //append empty line if success add any id into the tooltip
        if (addIdentifications(parent , tooltip))
            tooltip.add(LiteralText(""))
        addPowderSlots(parent, tooltip)
        addItemSuffix(parent, tooltip)
        addRestriction(parent, tooltip)
        return tooltip
    }
}