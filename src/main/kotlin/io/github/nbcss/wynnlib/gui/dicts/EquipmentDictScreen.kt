package io.github.nbcss.wynnlib.gui.dicts

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.i18n.Translations.UI_EQUIPMENTS
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

/**
 * The Dictionary UI for display all equipments.
 */
class EquipmentDictScreen(parent: Screen?) : DictionaryScreen<Equipment>(parent, TITLE) {
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:diamond_helmet")
        val TITLE: Text = UI_EQUIPMENTS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = EquipmentDictScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is EquipmentDictScreen
        }
    }

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
    }
}