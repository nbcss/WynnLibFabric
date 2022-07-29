package io.github.nbcss.wynnlib.gui.widgets

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.ability.AbilityTreeBuilderScreen
import io.github.nbcss.wynnlib.readers.AbilityTreeReader
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.LiteralText

class ATreeEditButtonWidget(private val parent: Screen,
                            private val character: CharacterClass, x: Int, y: Int):
    ButtonWidget(x, y, 20, 20, LiteralText("#"), PressAction { handlePress(it) }) {
    companion object {
        private fun handlePress(button: ButtonWidget) {
            if (button is ATreeEditButtonWidget) {
                button.active = false
                AbilityTreeReader.read(button.character){
                    val tree = AbilityRegistry.fromCharacter(button.character)
                    val fixed: MutableSet<Ability> = it.getActiveAbilities().toMutableSet()
                    tree.getMainAttackAbility()?.let { mainAttack -> fixed.add(mainAttack) }
                    val screen = AbilityTreeBuilderScreen(button.parent, tree, it.getMaxPoints(), fixed)
                    MinecraftClient.getInstance().setScreen(screen)
                }
            }
        }
    }

    init {
        active = AbilityTreeReader.getReader() == null
    }
}