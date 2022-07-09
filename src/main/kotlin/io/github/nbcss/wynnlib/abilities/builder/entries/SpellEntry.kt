package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

open class SpellEntry(private val spell: SpellSlot,
                      root: Ability, icon: Identifier): PropertyEntry(root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry? {
            val property = BoundSpellProperty.from(ability)
            return if (property != null){
                SpellEntry(property.getSpell(), ability, texture)
            }else{
                null
            }
        }

        override fun getKey(): String = "SPELL"
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayNameText().append(" ${getTierText()}").formatted(Formatting.BOLD))
        tooltip.add(spell.getComboText(getAbility().getCharacter()))
        tooltip.add(LiteralText.EMPTY)
        tooltip.addAll(getDescriptionTooltip())
        //Add effect tooltip
        val propertyTooltip = getPropertiesTooltip()
        if (propertyTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(propertyTooltip)
        }
        val upgradeTooltip = getUpgradeTooltip()
        if (upgradeTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(upgradeTooltip)
        }
        return tooltip
    }

    fun getManaCost(): Int {
        return ManaCostProperty.from(this)?.getManaCost() ?: 0
    }

    override fun getKey(): String {
        return spell.name
    }

    override fun getSideText(): Text {
        ManaCostProperty.from(this)?.let {
            return Symbol.MANA.asText().append(" ").append(
                LiteralText("${it.getManaCost()}")
                    .formatted(Formatting.WHITE)
            )
        }
        return super.getSideText()
    }
}