package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element

object DamageProperty: AbilityProperty<DamageProperty.Damage> {
    const val KEY: String = "damage"
    override fun read(encoding: String): Damage {

        TODO("Not yet implemented")
    }

    override fun write(data: JsonElement): String? {
        TODO("Not yet implemented")
    }

    override fun getKey(): String = KEY

    class Damage {

        fun getHits(): Int {
            return 1
        }

        fun getTotalDamage(): Double {
            TODO()
        }

        fun getNeutralDamage(): Double {
            TODO()
        }

        fun getElementalDamage(element: Element): Double {
            TODO()
        }
    }
}