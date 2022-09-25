package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.registry.MaterialRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object MaterialMatcher : ItemMatcher {
    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): MatchableItem? {
        if (name.length > 2 && name.contains("✫")) {
            MaterialRegistry.fromItemName(name)?.let {
                return it
            }
        }
        return null
    }
}