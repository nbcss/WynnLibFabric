package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import net.minecraft.util.Identifier

class BasicEntry(root: Ability, icon: Identifier): PropertyEntry(root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry {
            return BasicEntry(ability, texture)
        }

        override fun getKey(): String {
            return "NEW"
        }
    }
}