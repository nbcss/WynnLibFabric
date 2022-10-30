package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.Ability

interface HealProperty {
    fun modifyHeal(ability: Ability, modifier: Double): AbilityProperty
}