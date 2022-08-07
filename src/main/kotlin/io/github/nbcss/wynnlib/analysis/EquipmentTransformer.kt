package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.equipments.analysis.AnalysisEquipment
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class EquipmentTransformer(stack: ItemStack,
                           equipment: RegularEquipment): TooltipTransformer {
    private val item = AnalysisEquipment(equipment, stack)

    companion object: TooltipTransformer.Factory() {
        override fun create(stack: ItemStack, item: BaseItem): TooltipTransformer? {
            if (item is RegularEquipment) {
                //val tooltip = stack.getTooltip(client.player, TooltipContext.Default.NORMAL)
                return EquipmentTransformer(stack, item)
            }
            return null
        }
    }

    override fun getTooltip(): List<Text> {
        return item.getTooltip()
    }
}