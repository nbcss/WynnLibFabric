package io.github.nbcss.wynnlib.abilities

interface AbilityBuild {
    fun getSpareAbilityPoints(): Int
    fun getArchetypePoint(archetype: Archetype): Int
    fun hasAbility(ability: Ability): Boolean
}