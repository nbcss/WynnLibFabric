package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.effects.spells.warrior.*
import io.github.nbcss.wynnlib.abilities.effects.spells.SpellUnlock
import net.minecraft.text.Text

interface AbilityEffect {
    companion object {
        private val FACTORY_MAP: Map<String, Factory> = mapOf(
            //Warrior Ability Effects
            "BASH" to BashSpell,
            "CHARGE" to ChargeSpell,
            "UPPERCUT" to UppercutSpell,
            "WAR_SCREAM" to WarScreamSpell,
            "CHEAPER_BASH_I" to CostModifier,
            "CHEAPER_CHARGE" to CostModifier,
            "CHEAPER_UPPERCUT_I" to CostModifier,
            "CHEAPER_BASH_II" to CostModifier,
            "CHEAPER_WAR_SCREAM" to CostModifier,
            "CHEAPER_UPPERCUT_II" to CostModifier,
            //Archer Ability Effects
            "ARROW_STORM" to SpellUnlock,
            "ESCAPE" to SpellUnlock,
            "ARROW_BOMB" to SpellUnlock,
            "ARROW_SHIELD" to SpellUnlock,
        )

        fun fromData(id: String, properties: JsonObject): AbilityEffect {
            return FACTORY_MAP.getOrDefault(id.uppercase(), BaseEffect.FACTORY).create(properties)
        }
    }

    /**
     * Get the effect items tooltip of the ability.
     *
     * @return effect tooltip, could be empty.
     */
    fun getEffectTooltip(): List<Text>

    /**
     * Get the property string for given key.
     * If the key does not exist, same key will be returned.
     *
     * @param key the key of the string
     * @return property string of given key, or the key itself if no associated property.
     */
    fun getPropertyString(key: String): String

    interface Factory {
        fun create(properties: JsonObject): AbilityEffect
    }
}