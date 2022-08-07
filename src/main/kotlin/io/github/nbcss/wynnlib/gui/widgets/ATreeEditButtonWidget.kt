package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.ability.AbilityTreeEditorScreen
import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.readers.AbilityTreeReader
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

class ATreeEditButtonWidget(private val parent: Screen,
                            private val character: CharacterClass, x: Int, y: Int):
    ButtonWidget(x, y, 20, 20, LiteralText("#"), PressAction { handlePress(it) }) {
    companion object {
        private fun handlePress(button: ButtonWidget) {
            if (button is ATreeEditButtonWidget) {
                AbilityTreeReader.readActiveNodes(button.character){
                    val tree = AbilityRegistry.fromCharacter(button.character)
                    val fixed: MutableSet<Ability> = it.getActiveAbilities().toMutableSet()
                    tree.getMainAttackAbility()?.let { mainAttack -> fixed.add(mainAttack) }
                    val screen = AbilityTreeEditorScreen(button.parent, tree, it.getMaxPoints(), fixed)
                    MinecraftClient.getInstance().setScreen(screen)
                }
                button.updateActive()
            }
        }
    }

    init {
        updateActive()
    }

    private fun updateActive() {
        active = AbilityTreeHandler.getCurrentProcessor() == null
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(matrices, mouseX, mouseY, delta)
        updateActive()
    }
}