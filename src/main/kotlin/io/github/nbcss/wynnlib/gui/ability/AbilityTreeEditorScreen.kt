package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.gui.widgets.ConfirmButtonWidget
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_CLICK_TO_UNLOCK
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_EMPTY_LIST
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_NEW_ABILITIES
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_POINTS
import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.readers.AbilityTreeUnlocker
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AbilityTreeEditorScreen(parent: Screen?,
                              tree: AbilityTree,
                              maxPoints: Int,
                              fixedAbilities: Set<Ability>):
    AbilityTreeBuilderScreen(parent, tree, maxPoints, fixedAbilities) {

    override fun copy(): AbilityTreeBuilderScreen {
        return AbilityTreeEditorScreen(parent, getAbilityTree(), getMaxPoints(), getFixedAbilities())
    }

    override fun init() {
        super.init()
        val x = windowX + 217
        val y = windowY + 32
        addDrawableChild(ConfirmButtonWidget({ confirm() }, object : TooltipProvider {
            override fun getTooltip(): List<Text> {
                return getConfirmTooltip()
            }
        }, this, x, y))
    }

    private fun getConfirmTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        val orders = getActivateOrders()
        if (orders.isEmpty()) {
            tooltip.add(TOOLTIP_ABILITY_EMPTY_LIST.formatted(Formatting.RED))
        }else{
            tooltip.add(TOOLTIP_ABILITY_CLICK_TO_UNLOCK.formatted(Formatting.GREEN))
            tooltip.add(LiteralText.EMPTY)
            val cost = orders.sumOf { it.getAbilityPointCost() }
            tooltip.add(TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY)
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(LiteralText("$cost").formatted(Formatting.WHITE)))
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_ABILITY_NEW_ABILITIES.formatted(Formatting.GRAY, label = null, orders.size))
            for (ability in orders) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(ability.formatted(ability.getTier().getFormatting())))
            }
        }
        return tooltip
    }

    private fun confirm() {
        val orders = getActivateOrders()
        if (orders.isNotEmpty()) {
            client!!.setScreen(parent)
            AbilityTreeUnlocker.activeNodes(getAbilityTree().character, orders)
        }
    }
}