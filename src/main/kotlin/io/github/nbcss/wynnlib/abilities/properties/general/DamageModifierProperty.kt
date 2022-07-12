package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DamageModifierProperty(ability: Ability, data: JsonElement):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<DamageModifierProperty> {
        private const val HITS_KEY: String = "hits"
        private const val DAMAGE_LABEL_KEY: String = "label"
        private const val NEUTRAL_DAMAGE_KEY: String = "neutral"
        override fun create(ability: Ability, data: JsonElement): DamageModifierProperty {
            return DamageModifierProperty(ability, data)
        }
        override fun getKey(): String = "damage_modifier"
    }
    private val modifier: DamageMultiplier
    init {
        val json = data.asJsonObject
        val hits = if (json.has(HITS_KEY)) json[HITS_KEY].asInt else 0
        val label = if (json.has(DAMAGE_LABEL_KEY))
            DamageMultiplier.Label.fromName(json[DAMAGE_LABEL_KEY].asString) else null
        val neutral = if (json.has(NEUTRAL_DAMAGE_KEY)) json[NEUTRAL_DAMAGE_KEY].asInt else 0
        val elementalDamage = mapOf(pairs = Element.values().map {
            val key = it.getKey().lowercase()
            it to if (json.has(key)) json[key].asInt else 0
        }.toTypedArray())
        modifier = DamageMultiplier(hits, label,neutral, elementalDamage)
        ability.putPlaceholder(HITS_KEY, hits.toString())
    }

    fun getDamageModifier(): DamageMultiplier = modifier

    override fun modify(entry: PropertyEntry) {
        DamageProperty.from(entry)?.let {
            val damage = it.getDamage().add(modifier, modifier.getDamageLabel())
            entry.setProperty(DamageProperty.getKey(), DamageProperty(it.getAbility(), damage))
        }
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        if(!modifier.isZero()){
            val color = if (modifier.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
            val total = Symbol.DAMAGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${signed(modifier.getTotalDamage())}%").formatted(color))
            modifier.getDamageLabel()?.let {
                total.append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
                    .append(it.formatted(Formatting.DARK_GRAY))
                    .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
            }
            tooltip.add(total)
            //add neutral damage
            if (modifier.getNeutralDamage() != 0){
                tooltip.add(
                    LiteralText("   (").formatted(Formatting.DARK_GRAY)
                        .append(Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD))
                        .append(": ${signed(modifier.getNeutralDamage())}%)"))
            }
            //add element damages
            Element.values().forEach {
                val value = modifier.getElementalDamage(it)
                if (value != 0){
                    tooltip.add(
                        LiteralText("   (").formatted(Formatting.DARK_GRAY)
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
        return tooltip
    }
}