package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_HP
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_LOSS
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MageOphanimProperty(ability: Ability,
                          private val lightOfOrb: LightOfOrb):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MageOphanimProperty> {
        private const val COUNT_KEY = "count"
        private const val HP_KEY = "health"
        private const val LOSE_KEY = "loss"
        override fun create(ability: Ability, data: JsonElement): MageOphanimProperty {
            val json = data.asJsonObject
            val count = if (json.has(COUNT_KEY)) json[COUNT_KEY].asInt else 0
            val health = if (json.has(HP_KEY)) json[HP_KEY].asInt else 0
            val attackLose = if (json.has(LOSE_KEY)) json[LOSE_KEY].asInt else 0
            return MageOphanimProperty(ability, LightOfOrb(count, health, attackLose))
        }
        override fun getKey(): String = "ophanim"
    }

    fun getLightOfOrb(): LightOfOrb = lightOfOrb

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(Symbol.ALTER_HITS.asText().append(" ")
            .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(lightOfOrb.count.toString()).formatted(Formatting.WHITE)))
        tooltip.add(Symbol.HEART.asText().append(" ")
            .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_HP.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(lightOfOrb.health.toString()).formatted(Formatting.WHITE)))
        tooltip.add(Symbol.HEART.asText().append(" ")
            .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_LOSS.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${lightOfOrb.healthLose}%").formatted(Formatting.WHITE)))
        return tooltip
    }

    class Modifier(ability: Ability,
                   private val modifier: LightOfOrb):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                val json = data.asJsonObject
                val count = if (json.has(COUNT_KEY)) json[COUNT_KEY].asInt else 0
                val health = if (json.has(HP_KEY)) json[HP_KEY].asInt else 0
                val attackLose = if (json.has(LOSE_KEY)) json[LOSE_KEY].asInt else 0
                return Modifier(ability, LightOfOrb(count, health, attackLose))
            }
            override fun getKey(): String = "ophanim_modifier"
        }

        fun getModifier(): LightOfOrb = modifier

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = ArrayList()
            if (modifier.count != 0){
                tooltip.add(Symbol.ALTER_HITS.asText().append(" ")
                    .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText(signed(modifier.count)).formatted(colorOf(modifier.count))))
            }
            if (modifier.health != 0){
                tooltip.add(Symbol.HEART.asText().append(" ")
                    .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_HP.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText(signed(modifier.health)).formatted(colorOf(modifier.health))))
            }
            if (modifier.healthLose != 0){
                tooltip.add(Symbol.HEART.asText().append(" ")
                    .append(TOOLTIP_ABILITY_MAGE_ORBS_OF_LIGHT_LOSS.formatted(Formatting.GRAY).append(": "))
                    .append(LiteralText("${signed(modifier.healthLose)}%")
                        .formatted(colorOf(-modifier.healthLose))))
            }
            return tooltip
        }

        override fun modify(entry: PropertyEntry) {
            MageOphanimProperty.from(entry)?.let {
                val upgrade = it.getLightOfOrb().upgrade(getModifier())
                entry.setProperty(MageOphanimProperty.getKey(), MageOphanimProperty(it.getAbility(), upgrade))
            }
        }
    }

    data class LightOfOrb(val count: Int,
                          val health: Int,
                          val healthLose: Int){
        fun upgrade(modifier: LightOfOrb): LightOfOrb {
            val count = this.count + modifier.count
            val health = this.health + modifier.health
            val healthLose = this.healthLose + modifier.healthLose
            return LightOfOrb(count, health, healthLose)
        }
    }
}