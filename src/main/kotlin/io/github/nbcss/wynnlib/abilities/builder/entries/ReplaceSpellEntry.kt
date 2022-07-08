package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.util.Identifier

class ReplaceSpellEntry(parent: PropertyEntry,
                        spell: SpellSlot,
                        root: Ability,
                        icon: Identifier): SpellEntry(spell, root, icon) {
    init {
        //Copy replace mana cost
        parent.getProperty(ManaCostProperty.getKey())?.let {
            val cost = (it as ManaCostProperty).getManaCost()
            setProperty(ManaCostProperty.getKey(), ManaCostProperty(root, cost))
        }
    }
}