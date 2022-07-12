package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Supplier

object MaterialMatcher: ItemMatcher {
    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        if (!item.hasCustomName())
            return null
        val name = item.name.asString()
        for (tier in Material.Tier.values()) {
            if (name.startsWith(Formatting.WHITE.toString()) && name.endsWith(tier.suffix)){
                return Supplier { Settings.getMaterialColor(tier) }
            }
        }
        return null
    }
}