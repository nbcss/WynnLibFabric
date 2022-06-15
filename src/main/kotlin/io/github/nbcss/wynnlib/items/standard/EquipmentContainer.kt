package io.github.nbcss.wynnlib.items.standard

import io.github.nbcss.wynnlib.data.EquipmentType
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface EquipmentContainer {
    fun getType(): EquipmentType
    fun getIcon(): ItemStack
    fun getTooltip(): List<Text>
}