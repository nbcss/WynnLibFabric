package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AbilityDamageModifierProperty(ability: Ability,
                                    private val modifiers: Map<String, DamageMultiplier>):
    AbilityProperty(ability) {
    companion object: Type<AbilityDamageModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): AbilityDamageModifierProperty {
            val json = data.asJsonObject
            val modifiers: MutableMap<String, DamageMultiplier> = linkedMapOf()
            for (entry in json.entrySet()) {
                val modifier = DamageMultiplier.fromJson(entry.value.asJsonObject, 0)
                modifiers[entry.key] = modifier
            }
            return AbilityDamageModifierProperty(ability, modifiers)
        }
        override fun getKey(): String = "ability_damage_modifiers"
    }

    override fun updateEntries(container: EntryContainer): Boolean {
        for (entry in modifiers.entries) {
            container.getEntry(entry.key)?.let {
                DamageProperty.from(it)?.let { property ->
                    val modifier = entry.value
                    val damage = property.getDamage().add(modifier, modifier.getDamageLabel())
                    it.setProperty(DamageProperty.getKey(), DamageProperty(it.getAbility(), damage))
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
            if(!modifier.isZero()){
                val color = if (modifier.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
                val total = Symbol.DAMAGE.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY))
                if (ability != null) {
                    total.append(LiteralText(" ($ability)").formatted(Formatting.GRAY))
                }
                total.append(LiteralText(": ").formatted(Formatting.GRAY))
                    .append(LiteralText("${signed(modifier.getTotalDamage())}%").formatted(color))
                modifier.getDamageLabel()?.let {
                    total.append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
                        .append(it.formatted(Formatting.DARK_GRAY))
                        .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
                }
                tooltip.add(total)
                //add neutral damage
                if (modifier.getNeutralDamage() != 0){
                    tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                            .append(Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD))
                            .append(": ${signed(modifier.getNeutralDamage())}%)"))
                }
                //add element damages
                Element.values().forEach {
                    val value = modifier.getElementalDamage(it)
                    if (value != 0){
                        tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                                .append(it.formatted(Formatting.DARK_GRAY, "tooltip.damage"))
                                .append(": ${signed(value)}%)"))
                    }
                }
            }
            if (modifier.getHits() != 0){
                val hitsColor = if (modifier.getHits() > 0) Formatting.WHITE else Formatting.RED
                tooltip.add(Symbol.HITS.asText().append(" ")
                        .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                        .append(LiteralText(signed(modifier.getHits())).formatted(hitsColor)))
            }
        }
        return tooltip
    }
}