package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.MaxProperty

open class IDTransferBooster(parent: Ability, json: JsonObject): BaseEffect(parent, json), MaxProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): IDTransferBooster {
            return IDTransferBooster(parent, properties)
        }
    }
    private val max: Int = MaxProperty.read(json)

    override fun getMaxValue(): Int = max
    //todo
}