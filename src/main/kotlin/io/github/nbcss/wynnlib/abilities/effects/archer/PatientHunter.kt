package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.MaxProperty

class PatientHunter(parent: Ability, json: JsonObject): BaseEffect(parent, json), MaxProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): PatientHunter {
            return PatientHunter(parent, properties)
        }
    }
    private val maxDamage: Int = MaxProperty.read(json)

    override fun getMaxValue(): Int = maxDamage
}