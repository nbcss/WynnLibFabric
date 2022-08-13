package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackDamageRawProperty(ability: Ability,
                                  private val damage: Int):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<MainAttackDamageRawProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackDamageRawProperty {
            return MainAttackDamageRawProperty(ability, data.asInt)
        }
        override fun getKey(): String = "main_attack_damage_raw"
    }

    fun getRawDamage(): Int = damage

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), damage.toString())
    }

    override fun modify(entry: PropertyEntry) {
        val property = MainAttackDamageRawProperty.from(entry)
        if (property != null) {
            val value = property.getRawDamage() + getRawDamage()
            entry.setProperty(getKey(), MainAttackDamageRawProperty(property.getAbility(), value))
        }else{
            entry.setProperty(getKey(), MainAttackDamageRawProperty(entry.getAbility(), getRawDamage()))
        }
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        if (damage == 0)
            return emptyList()
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(signed(damage)).formatted(Formatting.WHITE)))
    }
}