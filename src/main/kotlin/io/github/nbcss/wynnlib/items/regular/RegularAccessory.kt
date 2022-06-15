package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.utils.getItemById
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularAccessory(private val parent: RegularEquipment, json: JsonObject)
    : EquipmentContainer {
    private val type: EquipmentType
    private val texture: ItemStack

    init {
        type = Metadata.asEquipmentType(json.get("accessoryType").asString)!!
        texture = if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            getItemById(materials[0].toInt(), meta)
        } else {
            type.getIcon()
        }
    }

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack = texture

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(LiteralText(parent.getDisplayName()))
        return tooltip
    }

}