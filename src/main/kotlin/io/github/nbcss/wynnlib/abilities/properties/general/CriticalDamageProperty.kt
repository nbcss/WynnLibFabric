package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class CriticalDamageProperty(ability: Ability,
                             private val bonus: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<CriticalDamageProperty> {
        override fun create(ability: Ability, data: JsonElement): CriticalDamageProperty {
            return CriticalDamageProperty(ability, data.asInt)
        }
        override fun getKey(): String = "critical_damage"
    }

    fun getCriticalDamageBonus(): Int = bonus

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), "$bonus")
    }

    override fun getOverviewTip(): Text? {
        return LiteralText(Symbol.DAMAGE.icon).formatted(Formatting.AQUA).append(" ")
            .append(LiteralText("+$bonus%").formatted(Formatting.WHITE))
    }
}