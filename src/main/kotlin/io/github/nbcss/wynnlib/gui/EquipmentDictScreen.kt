package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.lang.Translations.UI_EQUIPMENTS
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

/**
 * The Dictionary UI for display all equipments.
 */
class EquipmentDictScreen : DictionaryScreen<Equipment>(TITLE) {
    companion object {
        val ICON: ItemStack = getItem("minecraft:diamond_helmet")
        val TITLE: Text = UI_EQUIPMENTS.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(): HandbookTabScreen = EquipmentDictScreen()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is EquipmentDictScreen
        }
    }

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
    }
}