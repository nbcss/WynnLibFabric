package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.registry.IngredientRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object IngredientMatcher: ItemMatcher {

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>): Ingredient? {
        if (name.length > 2 && name.contains("✫")) {
            IngredientRegistry.fromName(name.substring(2).split("§")[0])?.let { ing ->
                return ing
            }
        }
        return null
    }
}