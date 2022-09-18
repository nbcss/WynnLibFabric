package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import net.minecraft.item.ItemStack

interface EquipmentCategory: TooltipProvider {
    fun getType(): EquipmentType
    fun getIcon(): ItemStack
}