package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DamageProperty(ability: Ability, private val damage: DamageMultiplier):
    AbilityProperty(ability), SetupProperty {
    companion object: Type {
        private const val HITS_KEY: String = "hits"
        private const val DAMAGE_LABEL_KEY: String = "label"
        private const val NEUTRAL_DAMAGE_KEY: String = "neutral"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            val json = data.asJsonObject
            val hits = if (json.has(HITS_KEY)) json[HITS_KEY].asInt else 1
            val label = if (json.has(DAMAGE_LABEL_KEY))
                DamageMultiplier.Label.fromName(json[DAMAGE_LABEL_KEY].asString) else null
            val neutral = if (json.has(NEUTRAL_DAMAGE_KEY)) json[NEUTRAL_DAMAGE_KEY].asInt else 0
            val elementalDamage = mapOf(pairs = Element.values().map {
                val key = it.getKey().lowercase()
                it to if (json.has(key)) json[key].asInt else 0
            }.toTypedArray())
            val damage = DamageMultiplier(hits, label,neutral, elementalDamage)
            return DamageProperty(ability, damage)
        }
        override fun getKey(): String = "damage"
    }

    fun getDamage(): DamageMultiplier = damage

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(HITS_KEY, damage.getHits().toString())
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        if(!damage.isZero()){
            val color = if (damage.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
            val total = Symbol.DAMAGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${damage.getTotalDamage()}%").formatted(color))
            damage.getDamageLabel()?.let {
                total.append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
                    .append(it.formatted(Formatting.DARK_GRAY))
                    .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
            }
            tooltip.add(total)
            //add neutral damage
            if (damage.getNeutralDamage() != 0){
                tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                    .append(Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD))
                    .append(": ${damage.getNeutralDamage()}%)"))
            }
            //add element damages
            Element.values().forEach {
                val value = damage.getElementalDamage(it)
                if (value != 0){
                    tooltip.add(LiteralText("   (").formatted(Formatting.DARK_GRAY)
                        .append(it.formatted(Formatting.DARK_GRAY, "tooltip.damage"))
                        .append(": ${value}%)"))
                }
            }
        }
        if (damage.getHits() != 1){
            tooltip.add(Symbol.HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${damage.getHits()}").formatted(Formatting.WHITE)))
        }
        return tooltip
    }
}