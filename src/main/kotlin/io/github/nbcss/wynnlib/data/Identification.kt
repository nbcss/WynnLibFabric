package io.github.nbcss.wynnlib.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Version
import io.github.nbcss.wynnlib.utils.parseStyle
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import java.util.*
import java.util.regex.Pattern

data class Identification(val id: String,               //id used in translation key
                          val apiId: String,            //id used in equipment api data
                          val name: String,             //name used in ingredient api data
                          val displayName: String,      //display name in game
                          val suffix: String,           //value suffix (e.g. %)
                          val group: IdentificationGroup,
                          val inverted: Boolean,        //whether the id bonus is inverted (e.g. -cost)
                          val constant: Boolean         //whether the id range is always fixed to base
                          ): Keyed, Translatable {
    constructor(json: JsonObject) : this(
        json.get("id").asString,
        json.get("apiId").asString,
        json.get("name").asString,
        json.get("displayName").asString,
        json.get("suffix").asString,
        if(json.has("group")) IdentificationGroup.fromName(json.get("group").asString) else
            IdentificationGroup.MISC,
        if(json.has("inverted")) json.get("inverted").asBoolean else false,
        if(json.has("constant")) json.get("constant").asBoolean else false)

    companion object: Registry<Identification>() {
        private const val RESOURCE = "assets/wynnlib/data/Identifications.json"
        private val SPELL_PLACEHOLDER = Pattern.compile("\\{(sp\\d)}")
        private val NAME_MAP: MutableMap<String, Identification> = linkedMapOf()
        private val SUFFIX_NAME_MAP: MutableMap<String, Identification> = linkedMapOf()
        private val GROUP_MAP: MutableMap<IdentificationGroup, MutableList<Identification>> = linkedMapOf()

        override fun getFilename(): String = RESOURCE

        fun fromGroup(group: IdentificationGroup): List<Identification> = GROUP_MAP[group] ?: emptyList()

        fun fromName(name: String): Identification? {
            return NAME_MAP[name.uppercase()]
        }

        fun fromSuffixName(suffix: String, displayName: String): Identification? {
            return SUFFIX_NAME_MAP["$displayName@$suffix"]
        }

        override fun reload(array: JsonArray) {
            NAME_MAP.clear()
            SUFFIX_NAME_MAP.clear()
            GROUP_MAP.clear()
            super.reload(array)
        }

        override fun put(item: Identification) {
            NAME_MAP[item.name] = item
            SUFFIX_NAME_MAP["${item.displayName}@${item.suffix}"] = item
            GROUP_MAP.getOrPut(item.group) { mutableListOf() }.add(item)
            val matcher = SPELL_PLACEHOLDER.matcher(item.displayName)
            if (matcher.find()) {
                SpellSlot.fromKey(matcher.group(1))?.let { spell ->
                    val nameSet: MutableSet<String> = mutableSetOf(
                        spell.displayName
                    )
                    for (character in CharacterClass.values()) {
                        AbilityRegistry.fromCharacter(character).getSpellAbility(spell)?.let { ability ->
                            ability.getName()?.let { nameSet.add(it) }
                        }
                    }
                    for (name in nameSet) {
                        val displayName = item.displayName.replace("{${spell.key}}", name)
                        SUFFIX_NAME_MAP["${displayName}@${item.suffix}"] = item
                    }
                }
            }
            super.put(item)
        }

        override fun read(data: JsonObject): Identification {
            return Identification(data)
        }
    }

    fun translate(style: Formatting, character: CharacterClass? = null): MutableText {
        var string = translate().string
        val matcher = SPELL_PLACEHOLDER.matcher(string)
        if (matcher.find()) {
            SpellSlot.fromKey(matcher.group(1))?.let { spell ->
                var name = spell.translate().string
                if (character != null) {
                    AbilityRegistry.fromCharacter(character).getSpellAbility(spell)?.let {
                        name = it.translate().string
                    }
                }
                string = string.replace("{${spell.key}}", name)
            }
        }
        return LiteralText(parseStyle(string, style.toString())).formatted(style)
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.id.${getKey().lowercase()}"
    }
}
