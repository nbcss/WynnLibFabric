package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.TooltipProvider
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface EquipmentCategory: TooltipProvider {
    fun getType(): EquipmentType
    fun getIcon(): ItemStack
}