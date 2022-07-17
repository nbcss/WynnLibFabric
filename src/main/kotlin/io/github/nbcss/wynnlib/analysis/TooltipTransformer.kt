package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface TooltipTransformer {
    fun getTooltip(): List<Text>

    companion object {
        private val factories: List<Factory> = listOf(
            EquipmentTransformer
        )

        fun asTransformer(stack: ItemStack, item: BaseItem): TooltipTransformer? {
            return factories.firstNotNullOfOrNull { it.create(stack, item) }
        }
    }

    abstract class Factory {
        protected val client: MinecraftClient = MinecraftClient.getInstance()
        abstract fun create(stack: ItemStack, item: BaseItem): TooltipTransformer?
    }
}