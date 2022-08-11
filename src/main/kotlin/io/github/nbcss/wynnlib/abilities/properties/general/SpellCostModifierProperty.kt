package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SpellCostModifierProperty(ability: Ability,
                                private val modifiers: Map<SpellSlot, Int>):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<SpellCostModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): SpellCostModifierProperty {
            val json = data.asJsonObject
            val modifiers: MutableMap<SpellSlot, Int> = linkedMapOf()
            for (entry in json.entrySet()) {
                SpellSlot.fromName(entry.key)?.let {
                    modifiers[it] = entry.value.asInt
                }
            }
            return SpellCostModifierProperty(ability, modifiers)
        }
        override fun getKey(): String = "spell_cost_modifier"
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        for (entry in modifiers.entries) {
            val modifier = entry.value
            val formatting = if (modifier > 0) Formatting.RED else Formatting.WHITE
            val text = Symbol.MANA.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${signed(modifier)}%").formatted(formatting))
            AbilityRegistry.fromCharacter(getAbility().getCharacter()).getSpellAbility(entry.key)?.let {
                text.append(LiteralText(" (").formatted(Formatting.GRAY)
                    .append(it.formatted(Formatting.GRAY)).append(")"))
            }
            tooltip.add(text)
        }
        return tooltip
    }
}