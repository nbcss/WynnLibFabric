package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.registry.PowderRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text


object PowderMatcher : ItemMatcher {
    private val powderNamePattern = Regex.fromLiteral("Powder (?:I|II|III|IV|V|VI){1}$")

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>): MatchableItem? {
        if (name.contains(powderNamePattern)) {
            val powder = PowderRegistry.fromName(name.substring(2))
            return if (powder != null) SimpleAdaptor(powder) else null
        }
        return null
    }

    class SimpleAdaptor(private val item: Powder): MatchableItem {
        override fun getMatcherType(): MatcherType {
            return MatcherType.fromPowderTier(item.getTier())
        }

        override fun asBaseItem(): BaseItem {
            return item
        }
    }
}