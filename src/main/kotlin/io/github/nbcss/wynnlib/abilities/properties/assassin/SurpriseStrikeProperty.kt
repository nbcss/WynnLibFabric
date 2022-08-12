package io.github.nbcss.wynnlib.abilities.properties.assassin

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.abilities.properties.general.DamageBonusProperty
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SurpriseStrikeProperty(ability: Ability, bonus: Int):
    DamageBonusProperty(ability, bonus), SetupProperty, ModifiableProperty {
    companion object: Type<SurpriseStrikeProperty> {
        override fun create(ability: Ability, data: JsonElement): SurpriseStrikeProperty {
            return SurpriseStrikeProperty(ability, data.asInt)
        }
        override fun getKey(): String = "surprise_strike_bonus"
        private const val ABILITY_NAME = "SURPRISE_STRIKE"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), bonus.toString())
    }

    override fun getSuffix(): String = "%"

    override fun getDamageBonusLabel(): Text? {
        val name = AbilityRegistry.get(ABILITY_NAME)?.translate()?.string
        return if (name != null) {
            LiteralText(" ($name)").formatted(Formatting.DARK_GRAY)
        }else{
            null
        }
    }

    override fun modify(entry: PropertyEntry) {
        SurpriseStrikeProperty.from(entry)?.let {
            val upgrade = it.getDamageBonus() + getDamageBonus()
            entry.setProperty(getKey(), SurpriseStrikeProperty(it.getAbility(), upgrade))
        }
    }

    override fun inUpgrade(): Boolean = false

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}