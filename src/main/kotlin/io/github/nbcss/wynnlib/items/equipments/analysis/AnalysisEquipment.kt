package io.github.nbcss.wynnlib.items.equipments.analysis

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.*
import io.github.nbcss.wynnlib.items.equipments.regular.RegularArmour
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import io.github.nbcss.wynnlib.items.equipments.regular.RegularWeapon
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class AnalysisEquipment(private val parent: RegularEquipment,
                        private val stack: ItemStack): RolledEquipment {
    private val propertyMap: MutableMap<String, AnalysisProperty> = mutableMapOf(
        pairs = listOf(
            RequirementProperty(),
            IdentificationProperty(),
            PowderSpecialProperty(),
            PowderProperty(),
        ).map { it.getKey() to it }.toTypedArray()
    )
    //private val category: TooltipProvider?
    init {
        val category = parent.getCategory()
        if (category is Weapon) {
            propertyMap["CATEGORY"] = AnalysisWeapon(this)
        }else if (category is Wearable) {
            propertyMap["CATEGORY"] = AnalysisWearable(this)
        }
        val tooltip: List<Text> = stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
        var line = 0
        outer@ while (line < tooltip.size) {
            for (property in propertyMap.values) {
                val inc = property.set(tooltip, line)
                if (inc > 0) {
                    line += inc
                    continue@outer
                }
            }
            line += 1
        }
    }

    override fun meetLevelReq(): Boolean {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).meetLevelReq()
    }

    override fun meetClassReq(): Boolean {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).meetClassReq()
    }

    override fun meetQuestReq(): Boolean {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).meetQuestReq()
    }

    override fun meetSkillReq(skill: Skill): Boolean {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).meetSkillReq(skill)
    }

    override fun getPowderSpecial(): PowderSpecial? {
        return (propertyMap[PowderSpecialProperty.KEY] as PowderSpecialProperty).getPowderSpecial()
    }

    override fun getPowders(): List<Element> {
        return (propertyMap[PowderProperty.KEY] as PowderProperty).getPowders()
    }

    override fun getRoll(): Int {
        TODO("Not yet implemented")
    }

    override fun getTooltip(): List<Text> {
        val category = propertyMap["CATEGORY"]
        return if (category is TooltipProvider) category.getTooltip() else
            stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
    }

    override fun getIdentificationValue(id: Identification): Int {
        return (propertyMap[IdentificationProperty.KEY] as IdentificationProperty).getIdentificationValue(id)
    }

    override fun getTier(): Tier {
        return parent.getTier()
    }

    override fun getType(): EquipmentType {
        return parent.getType()
    }

    override fun getLevel(): IRange {
        val level = (propertyMap[RequirementProperty.KEY] as RequirementProperty).getLevel()
        return SimpleIRange(level, level)
    }

    override fun getClassReq(): CharacterClass? {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).getClassReq()
    }

    override fun getQuestReq(): String? {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).getQuestReq()
    }

    override fun getRequirement(skill: Skill): Int {
        return (propertyMap[RequirementProperty.KEY] as RequirementProperty).getRequirement(skill)
    }

    override fun getPowderSlot(): Int {
        return (propertyMap[PowderProperty.KEY] as PowderProperty).getPowderSlots()
    }

    override fun getRestriction(): Restriction? {
        TODO("Not yet implemented")
    }

    override fun isIdentifiable(): Boolean {
        return parent.isIdentifiable()
    }

    override fun asWeapon(): Weapon? {
        val category = propertyMap["CATEGORY"]
        return if(category is Weapon) category else null
    }

    override fun asWearable(): Wearable? {
        val category = propertyMap["CATEGORY"]
        return if(category is Wearable) category else null
    }

    override fun getKey(): String {
        return parent.getKey()
    }

    override fun getDisplayText(): Text {
        return parent.getDisplayText()
    }

    override fun getDisplayName(): String {
        return parent.getDisplayName()
    }

    override fun getIcon(): ItemStack {
        return stack
    }

    override fun getRarityColor(): Color {
        return Settings.getTierColor(getTier())
    }

    override fun getIdentificationRange(id: Identification): IRange {
        return parent.getIdentificationRange(id)
    }
}