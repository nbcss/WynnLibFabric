package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.data.SpellSlot
import io.github.nbcss.wynnlib.i18n.Translations
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BoundSpellProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return BoundSpellProperty(ability, data)
        }
        override fun getKey(): String = "spell"
    }
    private val spell: SpellSlot = SpellSlot.fromName(data.asString) ?: SpellSlot.SPELL_1

    fun getSpell(): SpellSlot = spell
}