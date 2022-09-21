package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.builder.TreeBuildData
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text

class TreeBuildSelectScreen(parent: Screen?,
                            factory: TabFactory,
                            private val builder: AbilityTreeBuilderScreen): AbilityBuildDictionaryScreen(parent) {
    companion object {
        fun ofFactory(builder: AbilityTreeBuilderScreen): TabFactory {
            return object: TabFactory {
                override fun getTabIcon(): ItemStack = ICON
                override fun getTabTitle(): Text = TITLE
                override fun createScreen(parent: Screen?): HandbookTabScreen =
                    TreeBuildSelectScreen(parent, this, builder)
                override fun isInstance(screen: HandbookTabScreen): Boolean = screen is TreeBuildSelectScreen
                override fun shouldDisplay(): Boolean = true
            }
        }
    }
    init {
        tabs.clear()
        tabs.add(builder.getFactory())
        tabs.add(factory)
    }

    override fun fetchItems(): Collection<TreeBuildData> {
        return super.fetchItems()
            .filter { it.getTree().character == builder.getAbilityTree().character }
            .filter { it != builder.getBuild() }
            .filter { it.getTotalCost() <= builder.getMaxPoints() }
            .filter { data -> builder.getFixedAbilities().all { data.hasAbility(it) } }
    }

    override fun onClickItem(item: TreeBuildData, button: Int) {
        if (button == 0){
            playSound(SoundEvents.ENTITY_ITEM_PICKUP)
            builder.setAbilities(item.getActiveAbilities())
            client!!.setScreen(builder)
        }
    }

    override fun drawImportTreeTab(matrices: MatrixStack, mouseX: Int, mouseY: Int) {}

    override fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {}

    override fun drawDictionaryTab(matrices: MatrixStack, mouseX: Int, mouseY: Int) {}

    override fun isOverCharacterTab(index: Int, mouseX: Int, mouseY: Int): Boolean {
        return false
    }

    override fun isOverImportTreeTab(mouseX: Int, mouseY: Int): Boolean {
        return false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (builder is AbilityTreeEditorScreen && keyCode == 256 && shouldCloseOnEsc()) {
            parent?.close()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}