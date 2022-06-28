package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.ManaCostDisplay
import io.github.nbcss.wynnlib.abilities.display.RangeDisplay
import io.github.nbcss.wynnlib.abilities.properties.ManaCostProperty
import io.github.nbcss.wynnlib.abilities.properties.RangeProperty
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
    private val range: Double?
    init {
        spell = SpellSlot.fromName(json["spell"].asString)!!
        mana = json[ManaCostProperty.KEY].asInt
        range = if (json.has(RangeProperty.KEY)) json[RangeProperty.KEY].asDouble else null
    }

    fun getSpell(): SpellSlot = spell

    fun getManaCost(): Int = mana

    fun getRange(): Double? = range

    override fun getEffectTooltip(): List<Text> {
        return listOf(ManaCostDisplay, RangeDisplay).map { it.getTooltip(this) }.flatten()
    }

    override fun getProperty(key: String): String {
        when (key.lowercase()){
            ManaCostProperty.KEY -> return "$mana"
            RangeProperty.KEY -> return range?.toString() ?: ""
        }
        return ""
    }
}