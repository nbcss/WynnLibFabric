package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject

class UnlockContainerEffect(json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(json: JsonObject): AbilityEffect {
                return UnlockContainerEffect(json)
            }
        }
    }
    private val container: String
    init {
        container = json["container"].asString
    }

    fun getContainer(): String = container
}