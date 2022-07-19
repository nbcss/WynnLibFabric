package io.github.nbcss.wynnlib.items.equipments.analysis

import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.*
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import java.util.regex.Pattern

class AnalysisWeapon(private val equipment: AnalysisEquipment,
                     weapon: Weapon): Weapon, TooltipProvider, AnalysisProperty {
    companion object {
        private val DAMAGE_PATTERN = Pattern.compile("Neutral Damage: (\\d+)-(\\d+)")
        private val ELEM_DAMAGE_PATTERN = Pattern.compile(" Damage: (\\d+)-(\\d+)")
    }
    private var attackSpeed: AttackSpeed = weapon.getAttackSpeed()
    private var damage: IRange = IRange.ZERO
    private val elemDamage: MutableMap<Element, IRange> = mutableMapOf()
    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(equipment.getDisplayText())
        tooltip.addAll(getDamageTooltip())
        addPowderSpecial(equipment, tooltip)
        tooltip.add(LiteralText.EMPTY)
        addRolledRequirements(equipment, tooltip)
        tooltip.add(LiteralText.EMPTY)
        if (addRolledIdentifications(equipment, tooltip))
            tooltip.add(LiteralText.EMPTY)
        //todo
        addItemSuffix(equipment, tooltip)
        return tooltip
    }

    override fun getDamage(): IRange {
        return damage
    }

    override fun getElementDamage(elem: Element): IRange {
        return elemDamage.getOrDefault(elem, IRange.ZERO)
    }

    override fun getAttackSpeed(): AttackSpeed {
        return attackSpeed
    }

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val text = tooltip[line].siblings[0]
        if (line == 1 && text.siblings.isNotEmpty()) {
            AttackSpeed.fromDisplayName(text.asString())?.let {
                attackSpeed = it
                return 1
            }
        }
        run {
            val matcher = DAMAGE_PATTERN.matcher(text.asString())
            if (matcher.find()) {
                val lower = matcher.group(1).toInt()
                val upper = matcher.group(2).toInt()
                damage = SimpleIRange(lower, upper)
                return 1
            }
        }
        if (text.siblings.size == 2){
            //matches elem damage
            Element.fromDisplayName(text.siblings[0].asString())?.let { elem ->
                val matcher = ELEM_DAMAGE_PATTERN.matcher(text.siblings[1].asString())
                if (matcher.find()) {
                    val lower = matcher.group(1).toInt()
                    val upper = matcher.group(2).toInt()
                    elemDamage[elem] = SimpleIRange(lower, upper)
                    return 1
                }
            }
        }
        //println(text)
        //println(text.siblings.size)
        return 0
    }

    override fun getKey(): String = "CATEGORY"
}