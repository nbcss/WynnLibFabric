package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer

open class UpgradeProperty(ability: Ability, data: JsonElement): ModifyProperty(ability, data) {
    companion object: Type<UpgradeProperty> {
        override fun create(ability: Ability, data: JsonElement): UpgradeProperty {
            return UpgradeProperty(ability, data)
        }
        override fun getKey(): String = "upgrade"
    }

    override fun updateEntries(container: EntryContainer) {
        getModifyEntries(container).forEach { entry ->
            modifyEntry(entry)
            entry.addUpgrade(getAbility())
        }
    }
}