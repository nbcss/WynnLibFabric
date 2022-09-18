package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.calculator.QualityCalculator
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.items.*
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import java.util.regex.Pattern

class WearableProperty(private val equipment: RolledEquipment):
    Wearable, TooltipProvider, AnalysisProperty {
    companion object {
        private val HEALTH_PATTERN = Pattern.compile(" Health: (\\+\\d+|-\\d+)")
        private val DEFENCE_PATTERN = Pattern.compile(" Defence: (\\+\\d+|-\\d+)")
    }
    private var health: Int = 0
    private val elemDefence: MutableMap<Element, Int> = linkedMapOf()

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(equipment.getDisplayText())
        tooltip.add(LiteralText.EMPTY)
        val size = tooltip.size
        tooltip.addAll(getDefenseTooltip())
        addPowderSpecial(equipment, tooltip)
        if(tooltip.size > size)
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
                .append(QualityCalculator.formattingQuality(quality))
        addRolledPowderSlots(equipment, tooltip)
        addItemSuffix(equipment, tooltip, equipment.getRoll())
        addRestriction(equipment, tooltip)
        return tooltip
    }

    override fun getHealth(): IRange {
        return SimpleIRange(health, health)
    }

    override fun getElementDefence(elem: Element): Int {
        return elemDefence.getOrDefault(elem, 0)
    }

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].asString() != "" || tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        val baseString = base.asString()
        if (baseString != ""){
            val matcher = HEALTH_PATTERN.matcher(baseString)
            if(matcher.find()){
                health = matcher.group(1).toInt()
                return 1
            }
        }else if(base.siblings.size == 2){
            Element.fromDisplayName(base.siblings[0].asString())?.let {
                val matcher = DEFENCE_PATTERN.matcher(base.siblings[1].asString())
                if (matcher.find()){
                    elemDefence[it] = matcher.group(1).toInt()
                }
            }
        }
        return 0
    }

    override fun getKey(): String = "CATEGORY"
}