package io.github.nbcss.wynnlib.matcher.color

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.function.Supplier

object IngredientColorMatcher: ColorMatcher {
    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        if (!item.hasCustomName())
            return null
        val name = item.name.asString()
        for (tier in Ingredient.Tier.values()) {
            if (name.startsWith(Formatting.GRAY.toString()) && name.endsWith(tier.suffix)){
                return Supplier { Settings.getIngredientColor(tier) }
            }
        }
        return null
    }
}