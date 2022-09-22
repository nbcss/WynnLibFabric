package io.github.nbcss.wynnlib.matcher.color

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.util.function.Supplier

object EquipmentColorMatcher: ColorMatcher {
    private val tierMap = mapOf(pairs = Tier.values().map { TextColor.fromFormatting(it.formatting) to it }.toTypedArray())
    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        tooltip.asSequence()
            .filter { it.siblings.isNotEmpty() }
            .map { it.siblings[0] }
            .forEach next@{
                var innerIt = it
                // Crafted
                if (it.siblings.isNotEmpty()){
                    innerIt = it.siblings[0]
                }
                val tier = tierMap[innerIt.style.color] ?: return@next
                if ("${tier.name.lowercase()} " in innerIt.asString().lowercase()){
                    return Supplier { Settings.getTierColor(tier) }
                }
            }
        /*.map { tierMap[it.string] to it.style.color }
            .filter { it.first != null && it.second == TextColor.fromFormatting(it.first!!.formatting) }
            .map { Supplier<Color?> { Settings.getTierColor(it.first!!) } }
            .firstOrNull()*/
        return null

        // example "Rare Item [0]"
    }
}