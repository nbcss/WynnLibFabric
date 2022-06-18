package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.lang.Translations
import io.github.nbcss.wynnlib.registry.PowderRegistry
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class MaterialDictScreen : DictionaryScreen<Material>(TITLE) {
    companion object {
        val ICON: ItemStack = getItem("minecraft:diamond_axe#66")
        val TITLE: Text = Translations.UI_MATERIALS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(): HandbookTabScreen = MaterialDictScreen()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is MaterialDictScreen
        }
    }

    override fun fetchItems(): Collection<Material> {
        return emptyList()
    }
}