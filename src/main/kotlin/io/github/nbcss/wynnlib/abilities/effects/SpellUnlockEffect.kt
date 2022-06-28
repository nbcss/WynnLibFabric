package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.SpellSlot
import net.minecraft.text.Text

class SpellUnlockEffect(json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(properties: JsonObject): AbilityEffect {
                return SpellUnlockEffect(properties)
            }
        }
    }
    private val spell: SpellSlot
    private val mana: Int
    init {
        spell = SpellSlot.fromName(json["spell"].asString)!!
        mana = json["mana_cost"].asInt
    }

    fun getSpell(): SpellSlot = spell

    fun getManaCost(): Int = mana

    override fun getEffectTooltip(): List<Text> {

        return emptyList()
    }

    override fun getProperty(key: String): String {
        return key
    }
}