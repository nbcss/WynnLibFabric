package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

open class PropertyEntry(private val root: Ability,
                         private val icon: Identifier): Keyed {
    companion object: Factory {
        private val factoryMap: Map<String, Factory> = mapOf(
            "SPELL" to SpellEntry,
            "REPLACE" to ReplaceSpellEntry,
            "EXTEND" to ExtendEntry,
        )

        fun createEntry(type: String,
                        container: EntryContainer,
                        ability: Ability,
                        texture: Identifier): PropertyEntry? {
            return (factoryMap[type.uppercase()] ?: this).create(container, ability, texture)
        }

        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry {
            return PropertyEntry(ability, texture)
        }
    }
    protected val properties: MutableMap<String, AbilityProperty> = LinkedHashMap()
    private val upgrades: MutableList<Ability> = ArrayList()

    fun getAbility(): Ability = root

    fun getProperty(key: String): AbilityProperty? = properties[key]

    fun setProperty(key: String, property: AbilityProperty) {
        properties[key] = property
    }

    fun addUpgrade(ability: Ability) {
        upgrades.add(ability)
    }

    fun getUpgrades(): List<Ability> {
        return upgrades
    }

    override fun getKey(): String = root.getKey()

    open fun getSideText(): Text = LiteralText.EMPTY

    fun getTexture(): Identifier = icon

    fun getTier(): Int {
        return 1 + getUpgrades().size
    }

    fun getTierText(): String {
        return when (getTier()){
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            10 -> "X"
            else -> "✰"
        }
    }

    interface Factory {
        fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry?
    }
}