package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class DamageProperty(ability: Ability,
                          private val damage: DamageMultiplier):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<DamageProperty> {
        override fun create(ability: Ability, data: JsonElement): DamageProperty {
            val damage = DamageMultiplier.fromJson(data.asJsonObject)
            return DamageProperty(ability, damage)
        }
        override fun getKey(): String = "damage"
    }

    fun getDamage(): DamageMultiplier = damage

    open fun getDamageSuffix(): Text? {
        damage.getDamageLabel()?.let {
            return LiteralText(" (").formatted(Formatting.DARK_GRAY)
                .append(it.formatted(Formatting.DARK_GRAY)).append(")")
        }
        return null
    }

    override fun getOverviewTip(): Text {
        val text = Symbol.DAMAGE.asText().append(" ").append(
            LiteralText("${damage.getTotalDamage()}%").formatted(Formatting.WHITE)
        )
        if (damage.getHits() > 1) {
            text.append(LiteralText("x${damage.getHits()}").formatted(Formatting.YELLOW))
        }
        return text
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("hits", damage.getHits().toString())
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        if(!damage.isZero()){
            val color = if (damage.getTotalDamage() < 0) Formatting.RED else Formatting.WHITE
            val total = Symbol.DAMAGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_TOTAL_DAMAGE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("${damage.getTotalDamage()}%").formatted(color))
            getDamageSuffix()?.let {
                total.append(it)
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