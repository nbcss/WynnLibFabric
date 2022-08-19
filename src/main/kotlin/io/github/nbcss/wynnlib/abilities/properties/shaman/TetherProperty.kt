package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.general.DamageModifierProperty
import io.github.nbcss.wynnlib.abilities.properties.general.DamageProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TetherProperty(ability: Ability,
                     damage: DamageMultiplier,
                     private val cost: Double,
                     private val healthLoss: Double,
                     private val maxLoss: Double): DamageProperty(ability, damage) {
    companion object: Type<TetherProperty> {
        override fun create(ability: Ability, data: JsonElement): TetherProperty {
            val json = data.asJsonObject
            val damageJson = if (json.has(DAMAGE_KEY)) json[DAMAGE_KEY].asJsonObject else JsonObject()
            val damage = DamageMultiplier.fromJson(damageJson)
            val cost = if (json.has(COST_KEY)) json[COST_KEY].asDouble else 0.0
            val hp = if (json.has(LOSS_KEY)) json[LOSS_KEY].asDouble else 0.0
            val maxLoss = if (json.has(MAX_LOSS_KEY)) json[MAX_LOSS_KEY].asDouble else 0.0
            return TetherProperty(ability, damage, cost, hp, maxLoss)
        }
        override fun getKey(): String = "tether"
        private const val DAMAGE_KEY = "damage"
        private const val COST_KEY = "cost"
        private const val LOSS_KEY = "health_lose"
        private const val MAX_LOSS_KEY = "max_lose"
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        super.writePlaceholder(container)
        container.putPlaceholder("tether.cost", removeDecimal(cost))
        container.putPlaceholder("tether.health_lose", removeDecimal(healthLoss))
        container.putPlaceholder("tether.max_lose", removeDecimal(maxLoss))
    }

    override fun getDamageSuffix(): Text? {
        getDamage().getDamageLabel()?.let {
            return LiteralText(" (").formatted(Formatting.DARK_GRAY)
                .append(it.formatted(Formatting.DARK_GRAY, null, removeDecimal(healthLoss)))
                .append(")")
        }
        return null
    }

    class Modifier(ability: Ability,
                   modifier: DamageMultiplier): DamageModifierProperty(ability, modifier) {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                val modifier = DamageMultiplier.fromJson(data.asJsonObject, 0)
                return Modifier(ability, modifier)
            }
            override fun getKey(): String = "tether_damage_modifier"
        }

        override fun modify(entry: PropertyEntry) {
            TetherProperty.from(entry)?.let {
                val damage = it.getDamage().add(getDamageModifier(), getDamageModifier().getDamageLabel())
                val property = TetherProperty(it.getAbility(), damage, it.cost, it.healthLoss, it.maxLoss)
                entry.setProperty(TetherProperty.getKey(), property)
            }
        }
    }
}