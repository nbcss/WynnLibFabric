package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.Identification

class IDConvertorProperty(ability: Ability):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<IDConvertorProperty> {
        override fun create(ability: Ability, data: JsonElement): IDConvertorProperty {
            val json = data.asJsonObject
            //todo
            return IDConvertorProperty(ability)
        }
        override fun getKey(): String = "id_convertor"
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}