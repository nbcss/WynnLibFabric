package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class PropertyEntry(private val root: Ability,
                    private val icon: Identifier) {
    private val properties: MutableMap<String, AbilityProperty> = LinkedHashMap()
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

    fun getSideText(): Text {
        if (root.getTier().getLevel() == 0){
            root.getProperty(ManaCostProperty.getKey())?.let {
                return Symbol.MANA.asText().append(" ").append(
                    LiteralText("${(it as ManaCostProperty).getManaCost()}").formatted(Formatting.BLACK)
                )
            }
        }
        return LiteralText.EMPTY
    }

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
}