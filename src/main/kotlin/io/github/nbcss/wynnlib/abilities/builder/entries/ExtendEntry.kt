package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.info.ExtendProperty
import net.minecraft.util.Identifier

/**
 * Extend a particular ability (parent).
 */
class ExtendEntry(private val parent: PropertyEntry,
                  root: Ability, icon: Identifier): PropertyEntry(root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry? {
            val property = ExtendProperty.from(ability)
            if (property != null){
                val parent = property.getParent(container)
                if (parent != null){
                    return ExtendEntry(parent, ability, texture)
                }
            }
            return null
        }

        override fun getKey(): String = "EXTEND"
    }
}