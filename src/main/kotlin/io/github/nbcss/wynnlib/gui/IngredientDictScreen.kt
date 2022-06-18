package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.lang.Translations.UI_INGREDIENTS
import io.github.nbcss.wynnlib.registry.IngredientRegistry
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

/**
 * The Dictionary UI for display all ingredients.
 */
class IngredientDictScreen : DictionaryScreen<Ingredient>(TITLE) {
    companion object {
        val ICON: ItemStack = getItem("minecraft:diamond_axe#94")
        val TITLE: Text = UI_INGREDIENTS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(): HandbookTabScreen = IngredientDictScreen()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is IngredientDictScreen
        }
    }

    override fun fetchItems(): Collection<Ingredient> {
        return IngredientRegistry.getAll()
    }
}