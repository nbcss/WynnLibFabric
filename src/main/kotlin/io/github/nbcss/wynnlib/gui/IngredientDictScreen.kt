package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

//fixme replace equipment with ingredient
class IngredientDictScreen : DictionaryScreen<Equipment>(TITLE) {
    companion object {
        val ICON: ItemStack = getItem("minecraft:diamond_axe#94")
        val TITLE: Text = Translatable.from("wynnlib.ui.ingredients").translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(): HandbookTabScreen = IngredientDictScreen()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is IngredientDictScreen
        }
    }

    override fun fetchItems(): Collection<Equipment> {
        return emptyList()
    }
}