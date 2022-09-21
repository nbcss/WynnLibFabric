package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class CrafterScreen(parent: Screen?) : HandbookTabScreen(parent, TITLE) {
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:crafting_table")
        val TITLE: Text = Translations.UI_CRAFTER.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = CrafterScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is CrafterScreen
            override fun shouldDisplay(): Boolean = true
        }
    }
    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //todo
    }
}