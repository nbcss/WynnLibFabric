package io.github.nbcss.wynnlib.gui.dicts

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.lang.Translations
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class MaterialDictScreen(parent: Screen?) : DictionaryScreen<Material>(parent, TITLE) {
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:diamond_axe#66")
        val TITLE: Text = Translations.UI_MATERIALS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = MaterialDictScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is MaterialDictScreen
        }
    }

    override fun fetchItems(): Collection<Material> {
        return emptyList()
    }
}