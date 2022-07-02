package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.util.function.Supplier

object EquipmentMatcher: ItemMatcher {
    //private val tierMap = mapOf(pairs = Tier.values().map { it.displayName to it }.toTypedArray())
    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        tooltip.asSequence()
            .filter { it.siblings.isNotEmpty() }
            .map { it.siblings[0] }
            .forEach {
                //todo map it to Supplier<Color?> if find associated tier
            }
            /*.map { tierMap[it.string] to it.style.color }
            .filter { it.first != null && it.second == TextColor.fromFormatting(it.first!!.formatting) }
            .map { Supplier<Color?> { Settings.getTierColor(it.first!!) } }
            .firstOrNull()*/
        return null
    }
}