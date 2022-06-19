package io.github.nbcss.wynnlib.gui

import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface TabFactory {
    fun getTabIcon(): ItemStack
    fun getTabTitle(): Text
    fun createScreen(parent: Screen?): HandbookTabScreen
    fun isInstance(screen: HandbookTabScreen): Boolean

    fun getTabTooltip(): List<Text> {
        return listOf(getTabTitle())
    }
}