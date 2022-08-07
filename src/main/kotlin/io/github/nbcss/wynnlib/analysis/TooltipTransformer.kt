package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.BaseItem
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*

interface TooltipTransformer {
    fun getTooltip(): List<Text>

    companion object {
        private val transformerCacheMap: MutableMap<String, TooltipTransformer?> = WeakHashMap()
        private val factories: List<Factory> = listOf(
            EquipmentTransformer
        )

        fun asTransformer(stack: ItemStack, item: BaseItem): TooltipTransformer? {
            val key = stack.writeNbt(NbtCompound()).toString()
            if (transformerCacheMap.containsKey(key)) {
                return transformerCacheMap[key]
            }
            val result = factories.firstNotNullOfOrNull { it.create(stack, item) }
            transformerCacheMap[key] = result
            return result
        }
    }

    abstract class Factory {
        protected val client: MinecraftClient = MinecraftClient.getInstance()
        abstract fun create(stack: ItemStack, item: BaseItem): TooltipTransformer?
    }
}