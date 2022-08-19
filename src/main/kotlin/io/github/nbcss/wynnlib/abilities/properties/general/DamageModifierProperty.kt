package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
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

open class DamageModifierProperty(ability: Ability,
                                  private val modifier: DamageMultiplier):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<DamageModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): DamageModifierProperty {
            val modifier = DamageMultiplier.fromJson(data.asJsonObject, 0)
            return DamageModifierProperty(ability, modifier)
        }
        override fun getKey(): String = "damage_modifier"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("hits", modifier.getHits().toString())
    }

    fun getDamageModifier(): DamageMultiplier = modifier

    override fun modify(entry: PropertyEntry) {
        DamageProperty.from(entry)?.let {
            val damage = it.getDamage().add(modifier, modifier.getDamageLabel())
            entry.setProperty(DamageProperty.getKey(), DamageProperty(it.getAbility(), damage))
        }
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
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