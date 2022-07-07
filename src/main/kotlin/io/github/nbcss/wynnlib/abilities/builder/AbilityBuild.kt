package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.Archetype

interface AbilityBuild {
    fun getSpareAbilityPoints(): Int
    fun getArchetypePoint(archetype: Archetype): Int
    fun hasAbility(ability: Ability): Boolean
}