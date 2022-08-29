package io.github.nbcss.wynnlib.abilities.properties.assassin

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.utils.removeDecimal

class SatsujinProperty(ability: Ability,
                       private val damageBonus: Double):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<SatsujinProperty> {
        private const val DAMAGE_BONUS_KEY = "damage_bonus"
        override fun create(ability: Ability, data: JsonElement): SatsujinProperty {
            val json = data.asJsonObject
            val damageBonus = if (json.has(DAMAGE_BONUS_KEY)) json[DAMAGE_BONUS_KEY].asDouble else 0.0
            return SatsujinProperty(ability, damageBonus)
        }
        override fun getKey(): String = "satsujin"
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("satsujin.damage_bonus", removeDecimal(damageBonus))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}