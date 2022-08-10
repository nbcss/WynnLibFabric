package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.info.ReplaceAbilityProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.text.MutableText
import net.minecraft.util.Identifier

class ReplaceSpellEntry(private val removing: Ability,
                        properties: List<AbilityProperty.Type<out AbilityProperty>>,
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
            ReplaceAbilityProperty.from(ability)?.let {
                it.getReplaceAbility()?.let { parent ->
                    BoundSpellProperty.from(parent)?.let { spell ->
                        if (container.getSlotEntry(spell.getSpell().name).isEmpty()) {
                            return null
                        }
                        val properties = it.getKeepProperties()
                        return ReplaceSpellEntry(parent,
                            properties, container, spell.getSpell(),
                            ability, texture, upgradable)
                    }
                }
            }
            return null
        }
        override fun getKey(): String = "REPLACE"
    }
    init {
        for (type in properties) {
            val property = type.from(removing)
            if (property != null) {
                property.copy(root)?.let {
                    setProperty(type.getKey(), it)
                }
            }
        }
    }

    fun getRemovingAbility(): Ability {
        return removing
    }

    override fun getDisplayNameText(): MutableText {
        return getAbility().translate()
            .formatted(Ability.Tier.ofCharacter(getAbility().getCharacter()).getFormatting())
    }
}