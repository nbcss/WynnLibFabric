package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import net.minecraft.util.Identifier

class BasicEntry(container: EntryContainer,
                 root: Ability,
                 icon: Identifier,
                 upgradable: Boolean): PropertyEntry(container, root, icon, upgradable) {
    companion object: Factory {
        override fun create(container: EntryContainer,
                            ability: Ability,
                            texture: Identifier,
                            upgradable: Boolean): PropertyEntry {
            return BasicEntry(container, ability, texture, upgradable)
        }

        override fun getKey(): String {
            return "NEW"
        }
    }
}