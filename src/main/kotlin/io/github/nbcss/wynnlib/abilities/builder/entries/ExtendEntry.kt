package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.info.ExtendProperty
import net.minecraft.util.Identifier

/**
 * Extend a particular ability (parent).
 */
class ExtendEntry(private val parent: PropertyEntry,
                  root: Ability,
                  icon: Identifier,
                  upgradable: Boolean): PropertyEntry(root, icon, upgradable) {
    companion object: Factory {
        override fun create(container: EntryContainer,
                            ability: Ability,
                            texture: Identifier,
                            upgradable: Boolean): PropertyEntry? {
            val property = ExtendProperty.from(ability)
            if (property != null){
                val parent = property.getParent(container)
                if (parent != null){
                    return ExtendEntry(parent, ability, texture, upgradable)
                }
            }
            return null
        }

        override fun getKey(): String = "EXTEND"
    }
}