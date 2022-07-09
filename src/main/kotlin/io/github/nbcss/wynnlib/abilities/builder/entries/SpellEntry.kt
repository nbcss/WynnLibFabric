package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.general.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
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
            val property = ability.getProperty(BoundSpellProperty.getKey())
            return if (property is BoundSpellProperty){
                SpellEntry(property.getSpell(), ability, texture)
            }else{
                null
            }
        }

    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayNameText().formatted(Formatting.BOLD))
        tooltip.add(spell.getComboText(getAbility().getCharacter()))
        tooltip.add(LiteralText.EMPTY)
        tooltip.addAll(getDescriptionTooltip())
        //Add effect tooltip
        val propertyTooltip = getPropertiesTooltip()
        if (propertyTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(propertyTooltip)
        }
        return tooltip
    }

    fun getManaCost(): Int {
        val property = getProperty(ManaCostProperty.getKey())
        if (property is ManaCostProperty){
            return property.getManaCost()
        }
        return 0
    }

    override fun getKey(): String {
        return spell.name
    }

    override fun getSideText(): Text {
        getProperty(ManaCostProperty.getKey())?.let {
            return Symbol.MANA.asText().append(" ").append(
                LiteralText("${(it as ManaCostProperty).getManaCost()}")
                    .formatted(Formatting.WHITE)
            )
        }
        return super.getSideText()
    }
}