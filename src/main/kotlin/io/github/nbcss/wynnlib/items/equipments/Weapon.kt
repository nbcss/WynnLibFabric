package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.PowderSpecialAbility
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.roundToInt

interface Weapon {
    /**
     * Get the neutral damage range of the weapon.
     *
     * @return the neutral damage range of the weapon.
     */
    fun getDamage(): IRange

    /**
     * Get the element damage range of the weapon.
     *
     * @param elem: a non-null element key.
     * @return the element damage range of the given element.
     */
    fun getElementDamage(elem: Element): IRange
    
    fun getAttackSpeed(): AttackSpeed

    fun getDPS(): Double {
        return getAttackSpeed().speedModifier * ((getDamage().upper() + getDamage().lower()) +
                Element.values().map { getElementDamage(it) }.sumOf { it.upper() + it.lower() }) / 2.0
    }

    fun getPowderSpecialAbility(): PowderSpecialAbility? = null

    fun getDamageTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(getAttackSpeed().formatted(Formatting.GRAY))
        tooltip.add(LiteralText.EMPTY)
        val damage = getDamage()
        if(!damage.isZero()){
            val text = LiteralText(": ${damage.lower()}-${damage.upper()}")
            tooltip.add(
                Translations.TOOLTIP_NEUTRAL_DAMAGE.formatted(Formatting.GOLD)
                .append(text.formatted(Formatting.GOLD)))
        }
        Element.values().forEach {
            val range: IRange = getElementDamage(it)
            if (!range.isZero()) {
                val text = LiteralText(": ${range.lower()}-${range.upper()}")
                val prefix = it.formatted(Formatting.GRAY, "tooltip.damage")
                tooltip.add(prefix.append(text.formatted(Formatting.GRAY)))
            }
        }
        tooltip.add(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_AVERAGE_DAMAGE.formatted(Formatting.DARK_GRAY).append(": "))
            .append(LiteralText("${getDPS().roundToInt()}").formatted(Formatting.GRAY)))
        getPowderSpecialAbility()?.let {
            tooltip.addAll(it.getTooltip().map { line -> LiteralText("  ").append(line) })
        }
        return tooltip
    }
}