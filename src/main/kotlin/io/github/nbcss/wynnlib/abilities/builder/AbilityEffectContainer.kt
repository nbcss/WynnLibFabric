package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.abilities.properties.ManaCostModifierProperty
import io.github.nbcss.wynnlib.registry.AbilityRegistry

class AbilityEffectContainer(abilities: Collection<Ability>) {
    private val entries: MutableMap<String, AbilityEffectEntry>
    init {
        entries = LinkedHashMap()
        for (ability in abilities.sortedWith { o1, o2 ->
            val s1 = if (o1!!.getTier().getLevel() != 0) {
                5
            } else {
                val property = o1.getProperty(BoundSpellProperty.getKey())
                if (property is BoundSpellProperty) {
                    property.getSpell().ordinal
                } else {
                    5
                }
            }
            val s2 = if (o2!!.getTier().getLevel() != 0) {
                5
            } else {
                val property = o2.getProperty(BoundSpellProperty.getKey())
                if (property is BoundSpellProperty) {
                    property.getSpell().ordinal
                } else {
                    5
                }
            }
            s1.compareTo(s2)
        }) {
            if (ability.getTier().getLevel() == 0){
                println("add " + ability.getKey())
                entries[ability.getKey()] = AbilityEffectEntry(ability)
            }else{
                ability.getProperty(BoundSpellProperty.getKey())?.let {
                    println("upgrade " + ability.getKey())
                    val spell = (it as BoundSpellProperty).getSpell()
                    val tree = AbilityRegistry.fromCharacter(ability.getCharacter())
                    tree.getSpellAbility(spell)?.let { parent ->
                        entries[parent.getKey()]?.addUpgrade(ability)
                    }
                }
            }
        }
    }

    fun getEntries(): List<AbilityEffectEntry> {
        return entries.values.toList()
    }
}