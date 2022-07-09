package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty

class KeysUpgradeProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return KeysUpgradeProperty(ability, data)
        }
        override fun getKey(): String = "keys_upgrade"
    }
    private val keys: List<String> = data.asJsonArray.map { it.asString }.toList()

    fun getUpgradeKeys(): List<String> = keys

    override fun updateEntries(container: EntryContainer) {
        keys.mapNotNull { container.getEntry(it) }.forEach { entry ->
            for (property in getAbility().getProperties()) {
                if (property is ModifiableProperty){
                    property.modify(entry)
                }
            }
            entry.addUpgrade(getAbility())
        }
    }
}