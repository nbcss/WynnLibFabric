package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.util.Identifier

class ReplaceSpellEntry(parent: PropertyEntry,
                        spell: SpellSlot,
                        root: Ability,
                        icon: Identifier): SpellEntry(spell, root, icon) {

}