package io.github.nbcss.wynnlib.abilities.properties.warrior

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.utils.removeDecimal

class MassacreProperty(ability: Ability,
                       private val info: Info):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<MassacreProperty> {
        private const val ATTACK_SPEED_KEY: String = "attack_speed"
        private const val RATE_KEY: String = "rate"
        override fun create(ability: Ability, data: JsonElement): MassacreProperty {
            val json = data.asJsonObject
            val attackSpeed = if (json.has(ATTACK_SPEED_KEY))
                AttackSpeed.fromName(json[ATTACK_SPEED_KEY].asString) else AttackSpeed.SLOW
            val rate = if (json.has(RATE_KEY)) json[RATE_KEY].asDouble else 0.0
            return MassacreProperty(ability, Info(attackSpeed, rate))
        }
        override fun getKey(): String = "massacre"
    }

    fun getInfo(): Info = info

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("massacre.attack_speed") { info.attackSpeed.translate().string }
        container.putPlaceholder("massacre.rate", removeDecimal(info.chargeRate))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    data class Info(val attackSpeed: AttackSpeed,
                    val chargeRate: Double)
}