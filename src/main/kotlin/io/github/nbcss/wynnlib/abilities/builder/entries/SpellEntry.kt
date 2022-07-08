package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.data.SpellSlot
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

open class SpellEntry(private val spell: SpellSlot,
                      root: Ability, icon: Identifier): PropertyEntry(root, icon) {

    fun getManaCost(): Int {
        val property = getProperty(ManaCostProperty.getKey())
        if (property is ManaCostProperty){
            return property.getManaCost()
        }
        return 0
    }

    override fun getKey(): String {
        return spell.name
    }

    override fun getSideText(): Text {
        getProperty(ManaCostProperty.getKey())?.let {
            return Symbol.MANA.asText().append(" ").append(
                LiteralText("${(it as ManaCostProperty).getManaCost()}").formatted(Formatting.GRAY)
            )
        }
        return super.getSideText()
    }
}