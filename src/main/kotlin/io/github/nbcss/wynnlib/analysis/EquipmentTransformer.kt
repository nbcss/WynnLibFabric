package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.equipments.analysis.AnalysisEquipment
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class EquipmentTransformer(stack: ItemStack,
                           equipment: RegularEquipment): TooltipTransformer {
    private val item = AnalysisEquipment(equipment, stack)

    companion object: Factory {
        const val KEY = "EQUIPMENT"

        override fun create(stack: ItemStack, item: TransformableItem): TooltipTransformer? {
            if (item is RegularEquipment) {
                return EquipmentTransformer(stack, item)
            }
            return null
        }
        override fun getKey(): String = KEY
    }

    override fun getTooltip(): List<Text> {
        return item.getTooltip()
    }
}