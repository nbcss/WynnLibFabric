package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.PropertyProvider

interface AbilityProperty<T> {
    fun read(provider: PropertyProvider): T?
    //RANGE,          //Range
    //AOE,            //Area of Effect
    //MANA_COST,      //Mana Cost
    //Range, KeyCombo?, ManaCost, TotalDamage/ElementDamage, AOE,
    //Main ATK Damage/Range, 
}