package io.github.nbcss.wynnlib.analysis

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import java.util.*

abstract class TooltipTransformer(protected val stack: ItemStack) {
    protected val propertyMap: MutableMap<String, AnalysisProperty> = mutableMapOf()
    private val tooltipProperties: MutableList<AnalysisProperty> = mutableListOf()

    fun getTooltip(): List<Text> {
        return tooltipProperties.map { it.getTooltip() }.flatten()
    }

    fun init() {
        val tooltip: List<Text> = stack.getTooltip(MinecraftClient.getInstance().player,
            TooltipContext.Default.NORMAL)
        var line = 0
        outer@ while (line < tooltip.size) {
            for (property in propertyMap.values) {
                val inc = property.set(tooltip, line)
                if (inc > 0) {
                    line += inc
                    //todo
                    continue@outer
                }
            }

            line += 1
        }
    }

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