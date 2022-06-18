package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.lang.Translations
import io.github.nbcss.wynnlib.registry.PowderRegistry
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class PowderDictScreen : DictionaryScreen<Powder>(TITLE) {
    companion object {
        val ICON: ItemStack = getItem("minecraft:orange_dye")
        val TITLE: Text = Translations.UI_POWDERS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(): HandbookTabScreen = PowderDictScreen()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is PowderDictScreen
        }
    }

    override fun fetchItems(): Collection<Powder> {
        return PowderRegistry.getAll()
    }
}