package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.text.MutableText
import net.minecraft.util.Identifier

class ReplaceSpellEntry(parent: PropertyEntry,
                        container: EntryContainer,
                        spell: SpellSlot,
                        root: Ability,
                        icon: Identifier,
                        upgradable: Boolean): SpellEntry(spell, container, root, icon, upgradable) {
    companion object: Factory {
        override fun create(container: EntryContainer,
                            ability: Ability,
                            texture: Identifier,
                            upgradable: Boolean): PropertyEntry? {
            val property = BoundSpellProperty.from(ability)
            if (property != null){
                val current = container.getSlotEntry(property.getSpell().name)
                if (current != null){
                    return ReplaceSpellEntry(current, container, property.getSpell(), ability, texture, upgradable)
                }
            }
            return null
        }
        override fun getKey(): String = "REPLACE"
    }
    init {
        //Copy replace mana cost
        ManaCostProperty.from(parent)?.let {
            val cost = it.getManaCost()
            setProperty(ManaCostProperty.getKey(), ManaCostProperty(root, cost))
        }
    }

    override fun getDisplayNameText(): MutableText {
        return getAbility().translate()
            .formatted(Ability.Tier.ofCharacter(getAbility().getCharacter()).getFormatting())
    }
}