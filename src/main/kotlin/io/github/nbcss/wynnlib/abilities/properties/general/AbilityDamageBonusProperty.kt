package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AbilityDamageBonusProperty(ability: Ability,
                                 bonus: Int,
                                 private val target: String?):
    DamageBonusProperty(ability, bonus), SetupProperty, ModifiableProperty {
    companion object: Type<AbilityDamageBonusProperty> {
        override fun create(ability: Ability, data: JsonElement): AbilityDamageBonusProperty {
            val json = data.asJsonObject
            val target = if (json.has(TARGET_KEY)) json[TARGET_KEY].asString else null
            val bonus = if (json.has(BONUS_KEY)) json[BONUS_KEY].asInt else 0
            return AbilityDamageBonusProperty(ability, bonus, target)
        }
        override fun getKey(): String = "ability_damage_bonus"
        private const val TARGET_KEY = "ability"
        private const val BONUS_KEY = "bonus"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), bonus.toString())
    }

    override fun getSuffix(): String = "%"

    override fun getDamageBonusLabel(): Text? {
        if (target == null)
            return null
        val name = AbilityRegistry.get(target)?.translate()?.string
        return if (name != null) {
            LiteralText(" ($name)").formatted(Formatting.DARK_GRAY)
        }else{
            null
        }
    }

    override fun modify(entry: PropertyEntry) {
        AbilityDamageBonusProperty.from(entry)?.let {
            if (it.target == target) {
                val upgrade = it.getDamageBonus() + getDamageBonus()
                entry.setProperty(getKey(), AbilityDamageBonusProperty(it.getAbility(), upgrade, target))
            }
        }
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun inUpgrade(): Boolean = false
}