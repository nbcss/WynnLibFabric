package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class EquipmentTransformer(private val tooltip: List<Text>,
                           equipment: RegularEquipment): TooltipTransformer {
    companion object: TooltipTransformer.Factory() {
        override fun create(stack: ItemStack, item: BaseItem): TooltipTransformer? {
            if (item is RegularEquipment) {
                val tooltip = stack.getTooltip(client.player, TooltipContext.Default.NORMAL)
                return EquipmentTransformer(tooltip, item)
            }
            return null
        }
    }

    override fun getTooltip(): List<Text> {
        //todo
        return tooltip
    }
}