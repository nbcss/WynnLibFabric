package io.github.nbcss.wynnlib.gui

import net.minecraft.item.ItemStack
import net.minecraft.text.Text

interface TabFactory {
    fun getTabIcon(): ItemStack
    fun getTabTitle(): Text
    fun createScreen(): HandbookTabScreen
    fun isInstance(screen: HandbookTabScreen): Boolean

    fun getTabTooltip(): List<Text> {
        return listOf(getTabTitle())
    }
}