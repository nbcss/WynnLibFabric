package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.calculator.QualityCalculator.Companion.formattingQuality
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.*
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import java.util.regex.Pattern

class WeaponProperty(private val equipment: RolledEquipment):
    Weapon, TooltipProvider, AnalysisProperty {
    companion object {
        private val DAMAGE_PATTERN = Pattern.compile("Neutral Damage: (\\d+)-(\\d+)")
        private val ELEM_DAMAGE_PATTERN = Pattern.compile(" Damage: (\\d+)-(\\d+)")
    }
    private var attackSpeed: AttackSpeed? = null
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
        val lastSize = tooltip.size
        val quality = addRolledIdentifications(equipment, tooltip)
        if (tooltip.size > lastSize)
            tooltip.add(LiteralText.EMPTY)
        if (quality != null)
            tooltip[0] = LiteralText("")
                .append(tooltip[0]).append(" ")
                .append(formattingQuality(quality))
        addRolledPowderSlots(equipment, tooltip)
        addItemSuffix(equipment, tooltip, equipment.getRoll())
        addRestriction(equipment, tooltip)
        return tooltip
    }

    override fun getDamage(): IRange {
        return damage
    }

    override fun getElementDamage(elem: Element): IRange {
        return elemDamage.getOrDefault(elem, IRange.ZERO)
    }

    override fun getAttackSpeed(): AttackSpeed {
        return attackSpeed ?: AttackSpeed.NORMAL
    }

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if (attackSpeed == null && base.siblings.isEmpty()) {
            AttackSpeed.fromDisplayName(base.asString())?.let {
                attackSpeed = it
                return 1
            }
        }
        run {
            val matcher = DAMAGE_PATTERN.matcher(base.asString())
            if (matcher.find()) {
                val lower = matcher.group(1).toInt()
                val upper = matcher.group(2).toInt()
                damage = SimpleIRange(lower, upper)
                return 1
            }
        }
        if (base.siblings.size == 2){
            //matches elem damage
            Element.fromDisplayName(base.siblings[0].asString())?.let { elem ->
                val matcher = ELEM_DAMAGE_PATTERN.matcher(base.siblings[1].asString())
                if (matcher.find()) {
                    val lower = matcher.group(1).toInt()
                    val upper = matcher.group(2).toInt()
                    elemDamage[elem] = SimpleIRange(lower, upper)
                    return 1
                }
            }
        }
        return 0
    }

    override fun getKey(): String = "CATEGORY"
}