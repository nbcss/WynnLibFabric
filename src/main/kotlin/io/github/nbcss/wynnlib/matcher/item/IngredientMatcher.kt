package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.registry.IngredientRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object IngredientMatcher : ItemMatcher {

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): MatchableItem? {
        if (name.length > 2 && name.contains("✫")) {
            val ingredient = IngredientRegistry.fromName(name.substring(2).split("§")[0])
            return if (ingredient != null) SimpleAdaptor(ingredient) else null
        }
        return null
    }

    class SimpleAdaptor(private val item: Ingredient): MatchableItem {
        override fun getMatcherType(): MatcherType {
            return MatcherType.fromIngredientTier(item.getTier())
        }

        override fun asBaseItem(): BaseItem {
            return item
        }
    }
}