package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.IDTransferBooster
import io.github.nbcss.wynnlib.abilities.properties.legacy.ResistantBonusProperty

class TougherSkin(parent: Ability, json: JsonObject): IDTransferBooster(parent, json),
    ResistantBonusProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): TougherSkin {
            return TougherSkin(parent, properties)
        }
    }
    private val resistant: Int = ResistantBonusProperty.read(json)

    override fun getResistantBonus(): Int = resistant
}