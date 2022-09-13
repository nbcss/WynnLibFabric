package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.gui.widgets.ConfirmButtonWidget
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_CLICK_TO_MODIFY
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_EMPTY_LIST
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_NEW_ABILITIES
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_POINTS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_REMOVED_ABILITIES
import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.readers.AbilityTreeModifier
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AbilityTreeEditorScreen(parent: Screen?,
                              tree: AbilityTree,
                              maxPoints: Int,
                              fixedAbilities: Set<Ability>,
                              mutableAbilities: Set<Ability>):
    AbilityTreeBuilderScreen(parent, tree, maxPoints, fixedAbilities, mutableAbilities) {

    override fun copy(): AbilityTreeBuilderScreen {
        return AbilityTreeEditorScreen(parent, getAbilityTree(),
            getMaxPoints(), getFixedAbilities(), getMutableAbilities())
    }

    override fun init() {
        super.init()
        val x = windowX + 217
        val y = windowY + 31
        addDrawableChild(ConfirmButtonWidget({ confirm() }, object : TooltipProvider {
            override fun getTooltip(): List<Text> {
                return getConfirmTooltip()
            }
        }, this, x, y))

    }

    private fun getConfirmTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        val orders = getActivateOrders()
        val removed = getRemovedAbilities()
        if (orders.isEmpty() && removed.isEmpty()) {
            tooltip.add(TOOLTIP_ABILITY_EMPTY_LIST.formatted(Formatting.RED))
            return tooltip
        }
        tooltip.add(TOOLTIP_ABILITY_CLICK_TO_MODIFY.formatted(Formatting.GREEN))
        tooltip.add(LiteralText.EMPTY)
        val cost = removed.sumOf { it.getAbilityPointCost() } - orders.sumOf { it.getAbilityPointCost() }
        tooltip.add(TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY)
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(LiteralText(signed(cost)).formatted(Formatting.WHITE)))
        if (orders.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_ABILITY_NEW_ABILITIES.formatted(Formatting.GRAY, label = null, orders.size))
            for (ability in orders) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(ability.formatted(ability.getTier().getFormatting())))
            }
        }
        if (removed.isNotEmpty()) {
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_ABILITY_REMOVED_ABILITIES.formatted(Formatting.GRAY, label = null, removed.size))
            for (ability in removed) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(ability.formatted(ability.getTier().getFormatting())))
            }
        }
        return tooltip
    }

    private fun confirm() {
        val orders = getActivateOrders()
        val removed = getRemovedAbilities()
        if (orders.isNotEmpty() || removed.isNotEmpty()) {
            client!!.setScreen(parent)
            AbilityTreeModifier.modifyNodes(getAbilityTree().character, orders, removed.toList())
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == 256 && shouldCloseOnEsc()) {
            parent?.close()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}