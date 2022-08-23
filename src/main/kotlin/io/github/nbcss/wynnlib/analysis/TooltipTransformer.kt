package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import java.util.*

interface TooltipTransformer: TooltipProvider {

    fun init()

    companion object {
        private val transformerCacheMap: MutableMap<String, TooltipTransformer?> = WeakHashMap()
        private val factoryMap: Map<String, Factory> = mapOf(pairs = listOf(
            EquipmentTransformer
        ).map { it.getKey() to it }.toTypedArray())

        fun asTransformer(stack: ItemStack, item: TransformableItem): TooltipTransformer? {
            val key = stack.writeNbt(NbtCompound()).toString()
            if (transformerCacheMap.containsKey(key)) {
                return transformerCacheMap[key]
            }
            val result = factoryMap[item.getTransformKey()]?.create(stack, item)
            result?.init()
            transformerCacheMap[key] = result
            return result
        }
    }

    interface Factory: Keyed {
        fun create(stack: ItemStack, item: TransformableItem): TooltipTransformer?
    }
}