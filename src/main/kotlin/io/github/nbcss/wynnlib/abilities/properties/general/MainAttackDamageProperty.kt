package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackDamageProperty(ability: Ability,
                               private val damage: Int,
                               private val hits: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<MainAttackDamageProperty> {
        private const val DAMAGE_KEY = "damage"
        private const val HITS_KEY = "hits"
        override fun create(ability: Ability, data: JsonElement): MainAttackDamageProperty {
            val json = data.asJsonObject
            val damage = if (json.has(DAMAGE_KEY)) json[DAMAGE_KEY].asInt else 100
            val hits = if (json.has(HITS_KEY)) json[HITS_KEY].asInt else 1
            return MainAttackDamageProperty(ability, damage, hits)
        }
        override fun getKey(): String = "main_attack"
    }

    fun getMainAttackDamage(): Int = damage

    fun getMainAttackHits(): Int = hits

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("main_attack.damage", "$damage")
        container.putPlaceholder("main_attack.hits", "$hits")
    }

    override fun getOverviewTip(): Text {
        val text = Symbol.DAMAGE.asText().append(" ").append(
            LiteralText("$damage%").formatted(Formatting.WHITE)
        )
        if (hits > 1) {
            text.append(LiteralText("x$hits").formatted(Formatting.YELLOW))
        }
        return text
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${damage}%").formatted(Formatting.WHITE)))
        if (hits > 1) {
            tooltip.add(Symbol.HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText("$hits").formatted(Formatting.WHITE)))
        }
        return tooltip
    }

    class Modifier(ability: Ability,
                   private val damageModifier: Int,
                   private val hitsModifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                val json = data.asJsonObject
                val damage = if (json.has(DAMAGE_KEY)) json[DAMAGE_KEY].asInt else 0
                val hits = if (json.has(HITS_KEY)) json[HITS_KEY].asInt else 0
                return Modifier(ability, damage, hits)
            }
            override fun getKey(): String = "main_attack_modifier"
        }

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder("main_attack_modifier.damage", "$damageModifier")
            container.putPlaceholder("main_attack_modifier.hits", "$hitsModifier")
        }

        override fun modify(entry: PropertyEntry) {
            MainAttackDamageProperty.from(entry)?.let {
                val damage = it.getMainAttackDamage() + damageModifier
                val hits = it.getMainAttackHits() + hitsModifier
                val property = MainAttackDamageProperty(it.getAbility(), damage, hits)
                entry.setProperty(MainAttackDamageProperty.getKey(), property)
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            if (damageModifier != 0){
                tooltip.add(Symbol.DAMAGE.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText("${signed(damageModifier)}%").formatted(Formatting.WHITE)))
            }
            if (hitsModifier != 0){
                tooltip.add(Symbol.HITS.asText().append(" ")
                    .append(Translations.TOOLTIP_ABILITY_HITS.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText(signed(hitsModifier)).formatted(Formatting.WHITE)))
            }
            return tooltip
        }
    }
}