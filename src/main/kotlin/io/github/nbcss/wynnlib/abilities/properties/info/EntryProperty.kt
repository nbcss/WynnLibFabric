package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import net.minecraft.util.Identifier

class EntryProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type<EntryProperty> {
        private val UNKNOWN = Identifier("wynnlib", "textures/icons/unknown.png")
        private const val ICON_KEY: String = "icon"
        private const val TYPE_KEY: String = "type"
        override fun create(ability: Ability, data: JsonElement): EntryProperty {
            return EntryProperty(ability, data)
        }
        override fun getKey(): String = "entry"
    }
    private val info: Info = Info(data.asJsonObject)

    fun getEntryInfo(): Info = info

    override fun updateEntries(container: EntryContainer) {
        info.getFactory().create(container, getAbility(), info.getTexture())?.let { entry ->
            for (property in getAbility().getProperties()) {
                if (property is SetupProperty){
                    property.setup(entry)
                }
            }
            container.putEntry(entry)
        }
    }

    class Info(json: JsonObject) {
        private val icon: String? = if (json.has(ICON_KEY)) json[ICON_KEY].asString else null
        private val factory: PropertyEntry.Factory = PropertyEntry.getFactory(
            if (json.has(TYPE_KEY)) json[TYPE_KEY].asString else null)

        fun getTexture(): Identifier {
            return if (icon == null) UNKNOWN else
                Identifier("wynnlib", "textures/icons/${icon}.png")
        }

        fun getFactory(): PropertyEntry.Factory = factory
    }
}