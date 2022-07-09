package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty

class UpgradeProperty(ability: Ability, data: JsonElement): ModifyProperty(ability, data) {
    companion object: Type<UpgradeProperty> {
        override fun create(ability: Ability, data: JsonElement): UpgradeProperty {
            return UpgradeProperty(ability, data)
        }
        override fun getKey(): String = "upgrade"
    }

    override fun updateEntries(container: EntryContainer) {
        getModifyEntry(container)?.let { entry ->
            for (property in getAbility().getProperties()) {
                if (property is ModifiableProperty){
                    property.modify(entry)
                }
            }
            entry.addUpgrade(getAbility())
        }
    }
}