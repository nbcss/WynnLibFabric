package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.info.ModifyProperty
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ManaCostModifierProperty(ability: Ability,
                               private val modifier: Int):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<ManaCostModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): ManaCostModifierProperty {
            return ManaCostModifierProperty(ability, data.asInt)
        }
        override fun getKey(): String = "mana_modifier"
    }

    fun getManaModifier(): Int = modifier

    override fun modify(entry: PropertyEntry) {
        ManaCostProperty.from(entry)?.let {
            val cost = it.getManaCost() + modifier
            entry.setProperty(ManaCostProperty.getKey(), ManaCostProperty(it.getAbility(), cost))
        }
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val formatting = if (modifier > 0) Formatting.RED else Formatting.WHITE
        val text = Symbol.MANA.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MANA_COST.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(signed(modifier)).formatted(formatting))
        var ability: Ability? = null
        val upgradeProperty = UpgradeProperty.from(provider)
        if (upgradeProperty != null) {
            upgradeProperty.getUpgradingAbility()?.let { ability = it }
        }else{
            ModifyProperty.from(provider)?.getUpgradingAbility()?.let { ability = it }
        }
        if (ability != null) {
            text.append(LiteralText(" (").formatted(Formatting.GRAY)
                .append(ability!!.formatted(Formatting.GRAY)).append(")"))
        }
        return listOf(text)
    }
}