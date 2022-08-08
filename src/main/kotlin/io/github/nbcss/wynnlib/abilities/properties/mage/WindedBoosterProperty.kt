package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ValidatorProperty

class WindedBoosterProperty(ability: Ability,
                            private val dependencies: List<String>):
    AbilityProperty(ability), ValidatorProperty {
    companion object: Type<WindedBoosterProperty> {
        override fun create(ability: Ability, data: JsonElement): WindedBoosterProperty {
            return WindedBoosterProperty(ability, data.asJsonArray.map { it.asString })
        }
        override fun getKey(): String = "winded_booster"
    }

    override fun validate(container: EntryContainer): Boolean {
        return dependencies.any { container.getEntry(it) != null }
    }


}