package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
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

class MainAttackHitsProperty(ability: Ability,
                             private val hits: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MainAttackHitsProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackHitsProperty {
            return MainAttackHitsProperty(ability, data.asInt)
        }
        override fun getKey(): String = "main_attack_hits"
    }

    fun getMainAttackHits(): Int = hits

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return if (getMainAttackHits() != 1){
            listOf(Symbol.HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("$hits").formatted(Formatting.WHITE)))
        }else{
            emptyList()
        }
    }

    class Modifier(ability: Ability,
                   private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "main_attack_hits_modifier"
        }

        fun getModifier(): Int = modifier

        override fun modify(entry: PropertyEntry) {
            MainAttackHitsProperty.from(entry)?.let {
                val hits = it.getMainAttackHits() + getModifier()
                entry.setProperty(MainAttackHitsProperty.getKey(), MainAttackHitsProperty(it.getAbility(), hits))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(Formatting.WHITE)))
        }
    }
}