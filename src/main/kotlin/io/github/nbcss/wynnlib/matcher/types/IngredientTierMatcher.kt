package io.github.nbcss.wynnlib.matcher.types

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.utils.JsonGetter
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class IngredientTierMatcher(private val tier: Ingredient.Tier):
    AbstractMatcherType(tier.color), ProtectableType {
    companion object {
        fun keyOf(tier: Ingredient.Tier): String = "INGREDIENT_" + tier.name
    }
    private var protected: Boolean = false

    override fun getDisplayText(): Text {
        return formatted(Formatting.GRAY)
    }

    override fun getKey(): String {
        return keyOf(tier)
    }

    override fun reload(data: JsonObject) {
        super.reload(data)
        this.protected = JsonGetter.getOr(data, "protected", false)
    }

    override fun isProtected(): Boolean = protected

    override fun setProtected(value: Boolean) {
        this.protected = value
        Settings.markDirty()
    }
}