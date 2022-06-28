package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.utils.Keyed

interface AbilityProperty<T>: Keyed {
    fun read(encoding: String): T
    fun write(data: JsonElement): String?
    fun getValue(provider: PropertyProvider): T? {
        return if (provider.hasProperty(getKey())) read(provider.getProperty(getKey())) else null
    }

    companion object {
        private val propertyMap: Map<String, AbilityProperty<*>> = mapOf(
            ManaCostProperty.getKey() to ManaCostProperty,
            RangeProperty.getKey() to RangeProperty,
            DamageProperty.getKey() to DamageProperty,
            DurationProperty.getKey() to DurationProperty
        )

        fun encode(key: String, value: JsonElement): String {
            return propertyMap[key]?.write(value) ?: value.toString()
        }
    }
    //RANGE,          //Range
    //AOE,            //Area of Effect
    //MANA_COST,      //Mana Cost
    //Range, KeyCombo?, ManaCost, TotalDamage/ElementDamage, AOE,
    //Main ATK Damage/Range, 
}