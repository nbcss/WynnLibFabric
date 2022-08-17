package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import net.minecraft.util.Identifier

class AbilityMetadata(val ability: Ability,
                      data: JsonObject) {
    companion object {
        private const val ICON_KEY: String = "icon"
        private const val TYPE_KEY: String = "type"
        private const val UPGRADABLE_KEY: String = "upgradable"
        private const val OVERVIEW_KEY: String = "overview"
    }
    private val icon: IconTexture = IconTexture.fromName(if (data.has(ICON_KEY)) data[ICON_KEY].asString else null)
    private val upgradeable: Boolean = if (data.has(UPGRADABLE_KEY)) data[UPGRADABLE_KEY].asBoolean else true
    private val overviews: List<String> = if (data.has(OVERVIEW_KEY))
        data[OVERVIEW_KEY].asJsonArray.map { it.asString } else emptyList()
    private val factory: PropertyEntry.Factory = PropertyEntry.getFactory(
        if (data.has(TYPE_KEY)) data[TYPE_KEY].asString else null)

    fun getTexture(): Identifier = icon.getTexture()

    fun getFactory(): PropertyEntry.Factory = factory

    fun getOverviewProperties(): List<AbilityProperty.Type<out AbilityProperty>> {
        return overviews.mapNotNull { AbilityProperty.getType(it) }
    }

    fun createEntry(container: EntryContainer): PropertyEntry? {
        val entry = getFactory().create(container, ability, getTexture(), upgradeable)
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