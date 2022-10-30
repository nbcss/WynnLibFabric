package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.HealProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AbilityHealModifier(ability: Ability,
                          private val modifiers: Map<String, Pair<String, Double>>):
    AbilityProperty(ability) {
    companion object: Type<AbilityHealModifier> {
        override fun create(ability: Ability, data: JsonElement): AbilityHealModifier {
            val json = data.asJsonObject
            val modifiers: MutableMap<String, Pair<String, Double>> = linkedMapOf()
            for (entry in json.entrySet()) {
                val obj = entry.value.asJsonObject
                val key = obj["key"].asString
                val modifier = obj["modifier"].asDouble
                modifiers[entry.key] = key to modifier
            }
            return AbilityHealModifier(ability, modifiers)
        }
        override fun getKey(): String = "ability_heal_modifier"
    }

    override fun updateEntries(container: EntryContainer): Boolean {
        for (entry in modifiers.entries) {
            container.getEntry(entry.key)?.let {
                it.getProperty(entry.value.first)?.let { property ->
                    if (property is HealProperty) {
                        val modified = property.modifyHeal(it.getAbility(), entry.value.second)
                        it.setProperty(entry.value.first, modified)
                    }
                }
            }
        }
        return true
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        for (entry in modifiers.entries) {
            val ability = AbilityRegistry.get(entry.key)?.let { return@let it.translate().string }
            val modifier = entry.value
            if(modifier.second != 0.0){
                val color = if (modifier.second < 0) Formatting.RED else Formatting.GREEN
                val total = Symbol.HEART.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_TOTAL_HEAL.formatted(Formatting.GRAY))
                if (ability != null) {
                    total.append(LiteralText(" ($ability)").formatted(Formatting.GRAY))
                }
                val value = (if (modifier.second > 0) "+" else "") + "${removeDecimal(modifier.second)}%"
                total.append(LiteralText(": ").formatted(Formatting.GRAY))
                    .append(LiteralText(value).formatted(color))
                tooltip.add(total)
            }
        }
        return tooltip
    }
}