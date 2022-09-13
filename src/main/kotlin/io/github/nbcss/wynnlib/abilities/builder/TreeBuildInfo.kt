package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.Archetype

interface TreeBuildInfo {
    fun getAbilities(): Set<Ability>
    fun hasAbility(ability: Ability): Boolean
    fun getSpareAbilityPoints(): Int
    fun getArchetypePoint(archetype: Archetype): Int
}