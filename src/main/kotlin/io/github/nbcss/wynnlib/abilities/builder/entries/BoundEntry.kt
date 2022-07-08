package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.info.BoundingExtendProperty
import io.github.nbcss.wynnlib.abilities.properties.info.ExtendProperty
import net.minecraft.util.Identifier

/**
 * Bound to a particular ability (parent), and will be overwritten by ReplaceEntry.
 */
class BoundEntry(private val parent: PropertyEntry,
                 root: Ability, icon: Identifier): PropertyEntry(root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry? {
            val property = ability.getProperty(BoundingExtendProperty.getKey())
            if (property is BoundingExtendProperty){
                val parent = property.getParent(container)
                if (parent != null){
                    return BoundEntry(parent, ability, texture)
                }
            }
            return null
        }
    }
}