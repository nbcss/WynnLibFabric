package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

abstract class AbilityProperty(private val ability: Ability) {
    companion object {
        private val factoryMap: Map<String, Factory> = mapOf(
            BoundSpellProperty.getKey() to BoundSpellProperty,
            ManaCostProperty.getKey() to ManaCostProperty,
            ManaCostModifierProperty.getKey() to ManaCostModifierProperty,
        )

        fun fromData(ability: Ability, key: String, data: JsonElement): AbilityProperty? {
            return (factoryMap[key.lowercase()] ?: return null).create(ability, data)
        }
    }

    fun getAbility(): Ability = ability

    open fun getTooltip(): List<Text> = emptyList()

    interface Factory: Keyed {
        fun create(ability: Ability, data: JsonElement): AbilityProperty?
    }
}