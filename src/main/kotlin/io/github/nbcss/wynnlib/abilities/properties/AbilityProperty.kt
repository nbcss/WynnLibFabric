package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.archer.*
import io.github.nbcss.wynnlib.abilities.properties.assassin.AssassinClonesProperty
import io.github.nbcss.wynnlib.abilities.properties.assassin.BloomAoEProperty
import io.github.nbcss.wynnlib.abilities.properties.assassin.MaxMarkedProperty
import io.github.nbcss.wynnlib.abilities.properties.assassin.SmokeBombProperty
import io.github.nbcss.wynnlib.abilities.properties.general.*
import io.github.nbcss.wynnlib.abilities.properties.info.*
import io.github.nbcss.wynnlib.abilities.properties.mage.*
import io.github.nbcss.wynnlib.abilities.properties.shaman.MaxPuppetProperty
import io.github.nbcss.wynnlib.abilities.properties.shaman.MaxTotemProperty
import io.github.nbcss.wynnlib.abilities.properties.shaman.ShamanBloodPoolProperty
import io.github.nbcss.wynnlib.abilities.properties.warrior.CorruptedProperty
import io.github.nbcss.wynnlib.abilities.properties.warrior.EnragedBlowProperty
import io.github.nbcss.wynnlib.abilities.properties.warrior.MantleResistanceProperty
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

@Suppress("UNCHECKED_CAST")
abstract class AbilityProperty(private val ability: Ability) {
    companion object {
        private val typeMap: Map<String, Type<out AbilityProperty>> = mapOf(
            pairs = listOf(
                ValuesProperty,
                UpgradeProperty,
                ModifyProperty,
                ExtendProperty,
                ReplaceAbilityProperty,
                BoundSpellProperty,
                TotalHealProperty,
                PulseHealProperty,
                SelfDamageProperty,
                TimeDilationProperty,
                CriticalDamageProperty,
                ElementMasteryProperty,
                ResistantBonusProperty,
                ElementConversionProperty,
                DamageBonusProperty,
                DamageBonusProperty.Raw,
                DamageBonusProperty.PerFocus,
                DamageBonusProperty.PerMarked,
                DamageIntervalProperty,
                DamageIntervalProperty.Modifier,
                BonusEffectProperty,
                IDModifierProperty,
                IDConvertorProperty,
                ChanceProperty,
                ChanceProperty.Modifier,
                ManaCostProperty,
                ManaCostModifierProperty,
                SpellCostModifierProperty,
                DamageProperty,
                DamageModifierProperty,
                AbilityDamageModifierProperty,
                TeleportSuccessionProperty,
                TimelockProperty,
                FluidHealProperty,
                MainAttackDamageProperty,
                MainAttackDamageModifierProperty,
                MainAttackRangeProperty,
                MainAttackRangeProperty.Modifier,
                MainAttackRangeProperty.Clear,
                MainAttackHitsProperty,
                MainAttackHitsProperty.Modifier,
                RangeProperty,
                RangeProperty.Modifier,
                RangeProperty.Clear,
                CooldownProperty,
                CooldownProperty.Modifier,
                DurationProperty,
                DurationProperty.Modifier,
                AreaOfEffectProperty,
                AreaOfEffectProperty.Modifier,
                AreaOfEffectProperty.Clear,
                ChargeProperty,
                ChargeProperty.Modifier,
                ArcherStreamProperty,
                ArcherStreamProperty.Modifier,
                ArcherSentientBowsProperty,
                ArcherSentientBowsProperty.Modifier,
                MaxTrapProperty,
                MaxTrapProperty.Modifier,
                MaxFocusProperty,
                MaxFocusProperty.Modifier,
                PatientHunterProperty,
                PatientHunterProperty.Modifier,
                MageManaBankProperty,
                MageManaBankProperty.Modifier,
                MageOphanimProperty,
                MageOphanimProperty.Modifier,
                MageLightweaverProperty,
                MageLightweaverProperty.Modifier,
                WindedBoosterProperty,
                MaxWindedProperty,
                MaxWindedProperty.Modifier,
                SmokeBombProperty,
                SmokeBombProperty.Modifier,
                AssassinClonesProperty,
                AssassinClonesProperty.Modifier,
                MaxMarkedProperty,
                MaxMarkedProperty.Modifier,
                BloomAoEProperty,
                MantleResistanceProperty,
                CorruptedProperty,
                CorruptedProperty.Modifier,
                EnragedBlowProperty,
                MaxTotemProperty,
                MaxTotemProperty.Modifier,
                MaxPuppetProperty,
                MaxPuppetProperty.Modifier,
                ShamanBloodPoolProperty,
                ShamanBloodPoolProperty.Modifier,
            ).map { it.getKey() to it }.toTypedArray()
        )

        fun getType(key: String): Type<out AbilityProperty>? {
            return typeMap[key.lowercase()]
        }

        fun fromData(ability: Ability, key: String, data: JsonElement): AbilityProperty? {
            return (getType(key) ?: return null).create(ability, data)
        }
    }

    open fun writePlaceholder(container: PlaceholderContainer) = Unit

    open fun updateEntries(container: EntryContainer): Boolean = true

    open fun copy(other: Ability): AbilityProperty? = null

    fun getAbility(): Ability = ability

    open fun getTooltip(provider: PropertyProvider = getAbility()): List<Text> = emptyList()

    interface Type<T: AbilityProperty>: Keyed {
        fun create(ability: Ability, data: JsonElement): T?
        fun from(provider: PropertyProvider): T? {
            return provider.getProperty(getKey()) as? T
        }
    }
}