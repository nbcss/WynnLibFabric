package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.data.SpellSlot

open class BoundSpellEffect(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    BoundSpellProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): BoundSpellEffect {
            return BoundSpellEffect(parent, properties)
        }
    }
    private val spell: SpellSlot = BoundSpellProperty.read(json)

    override fun getSpell(): SpellSlot = spell
}