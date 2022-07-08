package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.ManaCostProperty
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class AbilityEffectEntry(private val root: Ability) {
    private val upgrades: MutableList<Ability> = ArrayList()

    fun getAbility(): Ability = root

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

    fun getTexture(): Identifier {
        return Identifier("wynnlib", "textures/icons/unknown.png")
    }

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