package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.Emerald
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object EmeraldMatcher : ItemMatcher {
    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): MatchableItem? {
        if (name.lowercase().contains("emerald")) {
            return when (item.item) {
                Emerald.Tier.BASIC.icon.item -> SimpleAdaptor(Emerald(Emerald.Tier.BASIC))
                Emerald.Tier.BLOCK.icon.item -> SimpleAdaptor(Emerald(Emerald.Tier.BLOCK))
                Emerald.Tier.LIQUID.icon.item -> SimpleAdaptor(Emerald(Emerald.Tier.LIQUID))
                else -> null
            }
        }
        return null
    }

    class SimpleAdaptor(private val item: Emerald) : MatchableItem {
        override fun getMatcherType(): MatcherType = MatcherType.fromEmeraldTier(item.getTier())

        override fun asBaseItem(): BaseItem = item
    }
}