package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.builder.entries.ReplaceSpellEntry
import io.github.nbcss.wynnlib.abilities.builder.entries.SpellEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.general.BoundSpellProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.util.Identifier

class EntryProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        private val UNKNOWN = Identifier("wynnlib", "textures/icons/unknown.png")
        private const val ICON_KEY: String = "icon"
        private const val TYPE_KEY: String = "type"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return EntryProperty(ability, data)
        }
        override fun getKey(): String = "entry"
    }
    private val info: Info = Info(data.asJsonObject)

    fun getEntryInfo(): Info = info

    override fun getPriority(): Int {
        when (info.getType()){
            "SPELL" -> {
                val property = getAbility().getProperty(BoundSpellProperty.getKey())
                if (property is BoundSpellProperty){
                    return property.getSpell().ordinal
                }
            }
            "REPLACE" -> {
                val property = getAbility().getProperty(BoundSpellProperty.getKey())
                if (property is BoundSpellProperty){
                    return SpellSlot.values().size
                }
            }
        }
        return 999
    }

    override fun updateEntries(container: EntryContainer) {
        //fixme replace with factory
        val entry = when (info.getType()){
            "SPELL" -> {
                val property = getAbility().getProperty(BoundSpellProperty.getKey())
                println(property)
                if (property is BoundSpellProperty){
                    SpellEntry(property.getSpell(), getAbility(), info.getTexture())
                }else{
                    null
                }
            }
            "REPLACE" -> {
                val property = getAbility().getProperty(BoundSpellProperty.getKey())
                if (property is BoundSpellProperty){
                    val current = container.getEntry(property.getSpell().name)
                    if (current != null){
                        ReplaceSpellEntry(current, property.getSpell(), getAbility(), info.getTexture())
                    }else{
                        null
                    }
                }else{
                    null
                }
            }
            else -> PropertyEntry(getAbility(), info.getTexture())
        }
        if (entry != null)
            container.putEntry(entry)
    }

    class Info(json: JsonObject) {
        private val icon: String? = if (json.has(ICON_KEY)) json[ICON_KEY].asString else null
        private val type: String? = if (json.has(TYPE_KEY)) json[TYPE_KEY].asString else null
        fun getType(): String? = type

        fun getTexture(): Identifier {
            return if (icon == null) UNKNOWN else
                Identifier("wynnlib", "textures/icons/${icon}.png")
        }
    }
}