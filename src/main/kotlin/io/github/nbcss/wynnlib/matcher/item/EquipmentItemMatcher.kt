package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TextColor

object EquipmentItemMatcher: ItemMatcher {
    private val tierMap = mapOf(
        pairs = Tier.values().map { TextColor.fromFormatting(it.formatting) to it }.toTypedArray()
    )
    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): MatchableItem? {
        if (name.length > 2 && name.startsWith("§")){
            RegularEquipmentRegistry.fromName(name.substring(2))?.let { eq ->
                if (tooltip.asSequence()
                        .filter { it.siblings.isNotEmpty() }
                        .map { it.siblings[0] }
                        .any { it.asString().contains(eq.getTier().displayName) }) return eq
            }
        }
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
                    return UnknownItem(tier)
                }
            }
        return null
    }

    class UnknownItem(val tier: Tier): MatchableItem {
        override fun getMatcherType(): MatcherType {
            return MatcherType.fromItemTier(tier)
        }

        override fun asBaseItem(): BaseItem? = null
    }
}