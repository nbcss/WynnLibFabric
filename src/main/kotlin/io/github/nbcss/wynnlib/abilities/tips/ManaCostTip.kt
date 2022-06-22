package io.github.nbcss.wynnlib.abilities.tips

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import net.minecraft.text.Text

class ManaCostTip(json: JsonObject): EffectTip {
    companion object {
        val FACTORY = object: EffectTip.Factory {
            override fun create(json: JsonObject): EffectTip {
                return ManaCostTip(json)
            }
        }
    }

    init {

    }

    override fun addTip(ability: Ability, tooltip: MutableList<Text>) {
        //ability.getEffects().get()
    }
}