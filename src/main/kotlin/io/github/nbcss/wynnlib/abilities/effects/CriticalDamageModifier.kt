package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability

class CriticalDamageModifier(parent: Ability, json: JsonObject): BaseEffect(parent, json) {
    companion object: AbilityEffect.Factory {
        const val KEY: String = "critical_damage"
        override fun create(parent: Ability, properties: JsonObject): CriticalDamageModifier {
            return CriticalDamageModifier(parent, properties)
        }
    }
    private val criticalDamage: Int = if (json.has(KEY)) json[KEY].asInt else 0

    fun getCriticalDamage(): Int = 0
}