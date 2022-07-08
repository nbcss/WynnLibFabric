package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.UpgradeableProperty
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ManaCostModifierProperty(ability: Ability, data: JsonElement):
    AbilityProperty(ability), UpgradeableProperty {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): ManaCostModifierProperty {
            return ManaCostModifierProperty(ability, data)
        }
        override fun getKey(): String = "mana_modifier"
    }
    private val modifier: Int = data.asInt

    fun getManaModifier(): Int = modifier

    override fun upgrade(entry: PropertyEntry) {
        entry.getProperty(ManaCostProperty.getKey())?.let {
            val cost = (it as ManaCostProperty).getManaCost() + modifier
            entry.setProperty(ManaCostProperty.getKey(), ManaCostProperty(it.getAbility(), cost))
        }
    }

    override fun getTooltip(): List<Text> {
        val formatting = if (modifier > 0) Formatting.RED else Formatting.WHITE
        val text = Symbol.MANA.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(signed(modifier)).formatted(formatting))
        getAbility().getProperty(UpgradeProperty.getKey())?.let {
            (it as UpgradeProperty).getUpgradeSpell()?.let { slot ->
                val tree = AbilityRegistry.fromCharacter(getAbility().getCharacter())
                tree.getSpellAbility(slot)?.let { spell ->
                    text.append(LiteralText(" (").formatted(Formatting.GRAY)
                        .append(spell.formatted(Formatting.GRAY)).append(")"))
                }
            }
        }
        return listOf(text)
    }
}