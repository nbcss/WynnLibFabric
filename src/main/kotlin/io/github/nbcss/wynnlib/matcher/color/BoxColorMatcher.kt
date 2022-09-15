package io.github.nbcss.wynnlib.matcher.color

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import java.util.function.Supplier

object BoxColorMatcher : ColorMatcher {
    private val tierMap = mapOf(pairs = Tier.values().map { it.formatting.code to it }.toTypedArray())

    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        if (!item.name.toString().contains("Unidentified")) return null
        tooltip.asSequence()
            .filter { it.siblings.isNotEmpty() }
            .map { it.siblings[0] }
            .forEach {
                return Supplier { Settings.getTierColor(tierMap[it.string[1]] ?: return@Supplier null) }
            }
        return null
    }

}