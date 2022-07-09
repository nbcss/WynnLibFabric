package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import net.minecraft.util.Identifier

class AbilityMetadata(val ability: Ability,
                      data: JsonObject) {
    companion object {
        private const val ICON_KEY: String = "icon"
        private const val TYPE_KEY: String = "type"
    }
    private val icon: AbilityIcon = AbilityIcon.fromName(if (data.has(ICON_KEY)) data[ICON_KEY].asString else null)
    private val factory: PropertyEntry.Factory = PropertyEntry.getFactory(
        if (data.has(TYPE_KEY)) data[TYPE_KEY].asString else null)

    fun getTexture(): Identifier = icon.getTexture()

    fun getFactory(): PropertyEntry.Factory = factory

    fun createEntry(container: EntryContainer): PropertyEntry? {
        val entry = getFactory().create(container, ability, getTexture())
        if (entry != null){
            for (property in ability.getProperties()) {
                if (property is SetupProperty){
                    property.setup(entry)
                }
            }
        }
        return entry
    }
}