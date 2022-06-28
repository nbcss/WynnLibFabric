package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import net.minecraft.text.Text

interface AbilityEffect: PropertyProvider {

    fun getEffectTooltip(): List<Text>

    companion object {
        private val FACTORY_MAP: Map<String, Factory> = mapOf(
            "BASH" to SpellUnlockEffect.FACTORY,
            "CHARGE" to SpellUnlockEffect.FACTORY,
            "UPPERCUT" to SpellUnlockEffect.FACTORY,
            "WAR_SCREAM" to SpellUnlockEffect.FACTORY,
            "ARROW_STORM" to SpellUnlockEffect.FACTORY,
            "ESCAPE" to SpellUnlockEffect.FACTORY,
            "ARROW_BOMB" to SpellUnlockEffect.FACTORY,
            "ARROW_SHIELD" to SpellUnlockEffect.FACTORY,
        )

        fun fromData(id: String, properties: JsonObject): AbilityEffect {
            return FACTORY_MAP.getOrDefault(id.uppercase(), PropertyHolderEffect.FACTORY).create(properties)
        }
    }

    interface Factory {
        fun create(properties: JsonObject): AbilityEffect
    }
}