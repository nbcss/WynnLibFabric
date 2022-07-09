package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.general.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class ReplaceSpellEntry(parent: PropertyEntry,
                        spell: SpellSlot,
                        root: Ability,
                        icon: Identifier): SpellEntry(spell, root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry? {
            val property = ability.getProperty(BoundSpellProperty.getKey())
            if (property is BoundSpellProperty){
                val current = container.getEntry(property.getSpell().name)
                if (current != null){
                    return ReplaceSpellEntry(current, property.getSpell(), ability, texture)
                }
            }
            return null
        }
    }
    init {
        //Copy replace mana cost
        parent.getProperty(ManaCostProperty.getKey())?.let {
            val cost = (it as ManaCostProperty).getManaCost()
            setProperty(ManaCostProperty.getKey(), ManaCostProperty(root, cost))
        }
    }

    override fun getDisplayNameText(): MutableText {
        return getAbility().translate()
            .formatted(Ability.Tier.ofCharacter(getAbility().getCharacter()).getFormatting())
    }
}