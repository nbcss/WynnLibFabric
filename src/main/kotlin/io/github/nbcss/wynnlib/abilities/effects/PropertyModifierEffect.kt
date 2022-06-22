package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject

class PropertyModifierEffect(json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(json: JsonObject): AbilityEffect {
                return PropertyModifierEffect(json)
            }
        }
    }
    private val container: String
    private val property: String
    private val value: String
    init {
        container = json["container"].asString
        property = json["property"].asString
        value = json["value"].asString
    }

    fun getContainer(): String = container

    fun getProperty(): String = property

    fun getValue(): String = value
}