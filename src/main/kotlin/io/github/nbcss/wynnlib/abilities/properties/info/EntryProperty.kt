package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.util.Identifier

class EntryProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        private val UNKNOWN = Identifier("wynnlib", "textures/icons/unknown.png")
        private const val ICON_KEY: String = "icon"
        private const val REPLACE_KEY: String = "replace"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return EntryProperty(ability, data)
        }
        override fun getKey(): String = "entry"
    }
    private val info: Info = Info(data.asJsonObject)

    fun getEntryInfo(): Info = info

    override fun getPriority(): Int = 1

    override fun updateEntries(container: EntryContainer) {
        val entry = PropertyEntry(getAbility(), info.getTexture())
        if (!info.isReplacing()){
            container.setEntry(getAbility().getKey(), entry)
        }else{
            //todo
        }
    }

    class Info(json: JsonObject) {
        private val icon: String? = if (json.has(ICON_KEY)) json[ICON_KEY].asString else null

        fun getTexture(): Identifier {
            return if (icon == null) UNKNOWN else
                Identifier("wynnlib", "textures/icons/${icon}.png")
        }

        fun isReplacing(): Boolean = false
    }
}