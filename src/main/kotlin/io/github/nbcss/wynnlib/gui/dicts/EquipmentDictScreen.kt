package io.github.nbcss.wynnlib.gui.dicts

import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.widgets.AdvanceSearchPaneWidget
import io.github.nbcss.wynnlib.gui.widgets.criteria.CriteriaGroup
import io.github.nbcss.wynnlib.gui.widgets.criteria.IdentificationCriteriaGroup
import io.github.nbcss.wynnlib.gui.widgets.criteria.ItemTypeGroup
import io.github.nbcss.wynnlib.gui.widgets.criteria.RarityGroup
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
    private var filter: AdvanceSearchPaneWidget<Equipment>? = null

    override fun init() {
        super.init()
        val groups: MutableList<CriteriaGroup<Equipment>> = mutableListOf()
        groups.add(ItemTypeGroup(memory, this))
        groups.add(RarityGroup(memory, this))
        //groups.addAll(IdentificationCriteriaGroup.of(memory))
        filter = AdvanceSearchPaneWidget(this, groups,
            windowX + backgroundWidth, windowY + 28)
        //filter?.reload(memory)
    }

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
    }

    override fun getSearchPane(): AdvanceSearchPaneWidget<Equipment>? = filter
}