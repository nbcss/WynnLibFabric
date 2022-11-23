package io.github.nbcss.wynnlib.gui.dicts

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.widgets.AdvanceSearchPaneWidget
import io.github.nbcss.wynnlib.gui.dicts.filter.ItemTypeFilter
import io.github.nbcss.wynnlib.gui.dicts.filter.RarityFilter
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.i18n.Translations.UI_EQUIPMENTS
import io.github.nbcss.wynnlib.registry.CharmRegistry
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.registry.TomeRegistry
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
            override fun shouldDisplay(): Boolean = true
        }
    }
    private var advSearch: AdvanceSearchPaneWidget<Equipment>? = null

    override fun init() {
        super.init()
        advSearch = AdvanceSearchPaneWidget.Builder(this,
            windowX + backgroundWidth, windowY + 28)
            .filter(ItemTypeFilter(memory, this))
            .filter(RarityFilter(memory, this))
            .build()
        //filter?.reload(memory)
    }

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
            .plus(TomeRegistry.getAll())
            .plus(CharmRegistry.getAll())
    }

    override fun getSearchPane(): AdvanceSearchPaneWidget<Equipment>? = advSearch
}