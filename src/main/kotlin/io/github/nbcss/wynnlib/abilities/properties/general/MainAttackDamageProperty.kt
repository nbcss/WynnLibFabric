package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackDamageProperty(ability: Ability,
                               private val damage: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MainAttackDamageProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackDamageProperty {
            return MainAttackDamageProperty(ability, data.asInt)
        }
        override fun getKey(): String = "main_attack_damage"
    }

    fun getMainAttackDamage(): Int = damage

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${damage}%").formatted(Formatting.WHITE)))
    }
}