package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject

interface AbilityEffect {
    companion object {
        private val FACTORY_MAP: Map<String, Factory> = mapOf(
            "UNLOCK" to UnlockContainerEffect.FACTORY,
            "PROPERTY_MODIFIER" to PropertyModifierEffect.FACTORY
        )

        fun fromData(json: JsonObject): AbilityEffect? {
            return FACTORY_MAP[json["type"].asString.uppercase()]?.create(json)
        }
    }

    interface Factory {
        fun create(json: JsonObject): AbilityEffect?
    }
}