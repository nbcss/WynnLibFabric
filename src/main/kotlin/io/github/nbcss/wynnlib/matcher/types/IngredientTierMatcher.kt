package io.github.nbcss.wynnlib.matcher.types

import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class IngredientTierMatcher(private val tier: Ingredient.Tier): AbstractMatcherType(tier.color) {
    companion object {
        fun keyOf(tier: Ingredient.Tier): String = "INGREDIENT_" + tier.name
    }

    override fun getDisplayText(): Text {
        return formatted(Formatting.GRAY)
    }

    override fun getKey(): String {
        return keyOf(tier)
    }

}