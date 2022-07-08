package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.archer.ArcherSentientBowsProperty
import io.github.nbcss.wynnlib.abilities.properties.archer.ArcherStreamProperty
import io.github.nbcss.wynnlib.abilities.properties.archer.SelfDamageProperty
import io.github.nbcss.wynnlib.abilities.properties.general.*
import io.github.nbcss.wynnlib.abilities.properties.info.EntryProperty
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

abstract class AbilityProperty(private val ability: Ability) {
    companion object {
        private val factoryMap: Map<String, Factory> = mapOf(
            pairs = listOf(
                EntryProperty,
                UpgradeProperty,
                BoundSpellProperty,
                TotalHealProperty,
                PulseHealProperty,
                SelfDamageProperty,
                GrowingEffectProperty,
                CriticalDamageProperty,
                ElementMasteryProperty,
                ResistantBonusProperty,
                DamageBonusProperty,
                DamageBonusProperty.Raw,
                DamageBonusProperty.PerFocus,
                BonusEffectProperty,
                ManaCostProperty,
                ManaCostModifierProperty,
                DamageProperty,
                DamageModifierProperty,
                MainAttackDamageModifierProperty,
                MainAttackRangeModifierProperty,
                RangeProperty,
                RangeProperty.Modifier,
                CooldownProperty,
                CooldownProperty.Modifier,
                DurationProperty,
                DurationProperty.Modifier,
                AreaOfEffectProperty,
                AreaOfEffectProperty.Modifier,
                ChargeProperty,
                ChargeProperty.Modifier,
                ArcherStreamProperty,
                ArcherStreamProperty.Modifier,
                ArcherSentientBowsProperty,
                ArcherSentientBowsProperty.Modifier,
            ).map { it.getKey() to it }.toTypedArray()
        )

        fun fromData(ability: Ability, key: String, data: JsonElement): AbilityProperty? {
            return (factoryMap[key.lowercase()] ?: return null).create(ability, data)
        }
    }

    open fun updateEntries(container: EntryContainer) = Unit

    fun getAbility(): Ability = ability

    open fun getPriority(): Int = 999

    open fun getTooltip(): List<Text> = emptyList()

    interface Factory: Keyed {
        fun create(ability: Ability, data: JsonElement): AbilityProperty?
    }
}