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
            "DOUBLE_BASH" to BashUpgrade,
            "TOUGHER_SKIN" to TougherSkin,
            "VEHEMENT" to Vehement,
            "HEAVY_IMPACT" to HeavyImpact,
            "QUADRUPLE_BASH" to BashUpgrade,
            "FIREWORKS" to SpellDamageModifier,
            "FLYBY_JAB" to FlybyJab,
            "FLAMING_UPPERCUT" to FlamingUppercut,
            "HALF_MOON_SWIPE" to HalfMoonSwipe,
            "IRON_LUNGS" to SpellDamageModifier,
            //GENERALIST, BAKALS_GRASP, ENRAGED_BLOW, AMBIDEXTROUS,
            // INTOXICATING_BLOOD, REJUVENATING_SKIN
            // UNCONTAINABLE_CORRUPTION, MASSIVE_BASH, MASSACRE, DISCOMBOBULATE,
            // BLOOD_PACT, HAEMORRHAGE, BRINK_OF_MADNESS
            "COUNTER" to Counter,
            "MANTLE_OF_THE_BOVEMISTS" to MantleOfTheBovemists,
            "AERODYNAMICS" to BoundSpellEffect,
            "PROVOKE" to Provoke,
            "PRECISE_STRIKES" to CriticalDamageModifier,
            "AIR_SHOUT" to AirShout,
            "FLYING_KICK" to FlyingKick,
            "STRONGER_MANTLE" to StrongerMantle,
            "MANACHISM" to Manachism,
            "BOILING_BLOOD" to BoilingBlood,
            "RAGNAROKKR" to Ragnarokkr,
            "BURNING_HEART" to IDTransferBooster,
            "STRONGER_BASH" to SpellDamageModifier,
            "COLLIDE" to Collide,
            "COMET" to SpellDamageModifier,
            "RADIANT_DEVOTEE" to IDTransferBooster,
            "WHIRLWIND_STRIKE" to WhirlwindStrike,
            "MYTHRIL_SKIN" to MythrilSkin,
            "ARMOUR_BREAKER" to ArmourBreaker,
            "SHIELD_STRIKE" to ShieldStrike,
            "SPARKLING_HOPE" to SparklingHope,
            "TEMPEST" to Tempest,
            "SPIRIT_OF_THE_RABBIT" to SpiritOfTheRabbit,
            "AXE_KICK" to AxeKick,
            "RADIANCE" to Radiance,
            "CYCLONE" to Cyclone,
            "THUNDERCLAP" to Thunderclap,
            "SECOND_CHANCE" to SecondChance,
            "MARTYR" to Martyr,
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
            "WINDSTORM" to ArcherStreamBooster,
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
            "FIERCE_STOMP" to FierceStomp,
            "SCORCHED_EARTH" to ScorchedEarth,
            //More Traps, HomingShots, ShockingBomb
            "LEAP" to Leap,
            "BETTER_LEAP" to BetterLeap,
            "BETTER_ARROW_SHIELD" to BetterArrowShield,
            "MANA_TRAP" to ManaTrap,
            "ESCAPE_ARTIST" to EscapeArtist,
            "BETTER_GUARDIAN_ANGELS" to BetterGuardianAngels,
            //INITIATOR
            "CALL_OF_THE_HOUND" to CallOfTheHound,
            "ARROW_HURRICANE" to ArcherStreamBooster,
            "PRECISE_SHOT" to CriticalDamageModifier,
            "ROCKET_JUMP" to BoundSpellEffect,
            "SHRAPNEL_BOMB" to ShrapnelBomb,
            "DECIMATOR" to BoundSpellEffect,
            "STRONGER_HOOK" to StrongerHook,
            "GEYSER_STOMP" to GeyserStomp,
            "CREPUSCULAR_RAY" to CrepuscularRay,
            "GRAPE_BOMB" to GrapeBomb,
            "ELUSIVE" to BoundSpellEffect,
            "TANGLED_TRAPS" to TangledTraps,
            "SNOW_STORM" to SnowStorm,
            "ALL_SEEING_PANOPTES" to AllSeeingPanoptes,
            "MINEFIELD" to Minefield,
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