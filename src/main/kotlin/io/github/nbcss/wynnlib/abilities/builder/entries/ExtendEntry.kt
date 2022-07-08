package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.info.ExtendProperty
import net.minecraft.util.Identifier

class ExtendEntry(private val parent: String,
                  root: Ability, icon: Identifier): PropertyEntry(root, icon) {
    companion object: Factory {
        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry? {
            val property = ability.getProperty(ExtendProperty.getKey())
            return if (property is ExtendProperty){
                ExtendEntry(property.getExtendParent(), ability, texture)
            }else{
                null
            }
        }
    }

    fun getExtendParentKey(): String = parent
}