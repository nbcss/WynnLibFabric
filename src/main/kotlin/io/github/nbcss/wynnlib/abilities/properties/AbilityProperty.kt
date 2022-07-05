package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

abstract class AbilityProperty(private val ability: Ability) {
    companion object {
        private val factoryMap: Map<String, Factory> = mapOf(
            pairs = listOf(
                BoundSpellProperty,
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

    fun getAbility(): Ability = ability

    open fun getTooltip(): List<Text> = emptyList()

    interface Factory: Keyed {
        fun create(ability: Ability, data: JsonElement): AbilityProperty?
    }
}