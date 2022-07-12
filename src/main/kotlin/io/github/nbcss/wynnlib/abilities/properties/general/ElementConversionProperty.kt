package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.data.DamageMultiplier
import io.github.nbcss.wynnlib.data.Element

class ElementConversionProperty(ability: Ability,
                                private val element: Element):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<ElementConversionProperty> {
        override fun create(ability: Ability, data: JsonElement): ElementConversionProperty {
            val elem = Element.fromId(data.asString) ?: Element.AIR
            return ElementConversionProperty(ability, elem)
        }
        override fun getKey(): String = "element_convert"
    }

    override fun modify(entry: PropertyEntry) {
        DamageProperty.from(entry)?.let { property ->
            val damage = property.getDamage()
            val modified = DamageMultiplier(damage.getHits(), damage.getDamageLabel(), damage.getNeutralDamage(),
                mapOf(element to Element.values().sumOf { damage.getElementalDamage(it) }))
            entry.setProperty(DamageProperty.getKey(), DamageProperty(getAbility(), modified))
        }
    }
}