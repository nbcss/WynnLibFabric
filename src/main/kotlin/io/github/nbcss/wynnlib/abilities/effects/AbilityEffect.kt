package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.effects.warrior.*
import io.github.nbcss.wynnlib.abilities.effects.archer.*
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
            "WARRIOR_AIR_MASTERY" to ElementMastery,
            "WARRIOR_FIRE_MASTERY" to ElementMastery,
            "WARRIOR_WATER_MASTERY" to ElementMastery,
            "WARRIOR_THUNDER_MASTERY" to ElementMastery,
            "WARRIOR_EARTH_MASTERY" to ElementMastery,
            "SPEAR_PROFICIENCY_I" to WeaponProficiency,
            "SPEAR_PROFICIENCY_II" to WeaponProficiency,
            "DOUBLE_BASH" to DoubleBash,
            "HEAVY_IMPACT" to HeavyImpact,
            //Archer Ability Effects
            "ARROW_STORM" to ArrowStormSpell,
            "ESCAPE" to EscapeSpell,
            "ARROW_BOMB" to ArrowBombSpell,
            "ARROW_SHIELD" to ArrowShieldSpell,
            "CHEAPER_ARROW_BOMB_I" to CostModifier,
            "CHEAPER_ESCAPE_I" to CostModifier,
            "CHEAPER_ARROW_STORM_I" to CostModifier,
            "CHEAPER_ARROW_STORM_II" to CostModifier,
            "CHEAPER_ARROW_SHIELD" to CostModifier,
            "CHEAPER_ESCAPE_II" to CostModifier,
            "CHEAPER_ARROW_BOMB_II" to CostModifier,
            "ARCHER_AIR_MASTERY" to ElementMastery,
            "ARCHER_FIRE_MASTERY" to ElementMastery,
            "ARCHER_WATER_MASTERY" to ElementMastery,
            "ARCHER_THUNDER_MASTERY" to ElementMastery,
            "ARCHER_EARTH_MASTERY" to ElementMastery,
            "BOW_PROFICIENCY_I" to WeaponProficiency,
            "HEART_SHATTER" to HeartShatter,
            "WINDY_FEET" to WindyFeet,
            "ARROW_RAIN" to ArrowRain,
            "NIMBLE_STRING" to NimbleString,
            "PHANTOM_RAY" to PhantomRay,
            "FIRE_CREEP" to FireCreep,
            "BRYOPHYTE_ROOTS" to BryophyteRoots,
            "TRIPLE_SHOTS" to TripleShots,
            "FRENZY" to Frenzy,
            "GUARDIAN_ANGELS" to GuardianAngels,
            "FOCUS" to Focus,
            "BASALTIC_TRAP" to BasalticTrap,
            "WINDSTORM" to WindStorm,
            "GRAPPLING_HOOK" to GrapplingHook,
            "MORE_SHIELDS" to MoreShields,
            "MORE_FOCUS_I" to Focus,
            "MORE_FOCUS_II" to Focus,
            "IMPLOSION" to Implosion,
            "PATIENT_HUNTER" to PatientHunter,
            "STRONGER_PATIENT_HUNTER" to PatientHunter,
            "STORMY_FEET" to StormyFeet,
            "REFINED_GUNPOWDER" to SpellDamageModifier,
            "TRAVELER" to IDTransferBooster,
            "TWAINS_ARC" to TwainArc,
            "BOUNCING_BOMB" to BoundSpellEffect,
        )

        fun fromData(ability: Ability, properties: JsonObject): AbilityEffect {
            return FACTORY_MAP.getOrDefault(ability.getKey().uppercase(), BaseEffect.FACTORY)
                .create(ability, properties)
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

    /**
     * Get the parent ability of the effect.
     */
    fun getAbility(): Ability

    interface Factory {
        fun create(parent: Ability, properties: JsonObject): AbilityEffect
    }
}