package io.github.nbcss.wynnlib.items.equipments.analysis

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.TooltipProvider
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.IdentificationProperty
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.ItemProperty
import io.github.nbcss.wynnlib.items.equipments.regular.RegularArmour
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import io.github.nbcss.wynnlib.items.equipments.regular.RegularWeapon
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class AnalysisEquipment(private val parent: RegularEquipment,
                        private val stack: ItemStack): RolledEquipment {
    private val propertyMap: MutableMap<String, ItemProperty> = mutableMapOf(
        pairs = listOf(
            IdentificationProperty()
        ).map { it.getKey() to it }.toTypedArray()
    )
    //private val category: TooltipProvider?
    init {
        //if (parent.getCategory())
        //category = null //todo
        val category = parent.getCategory()
        if (category is RegularWeapon) {
            propertyMap["CATEGORY"] = AnalysisWeapon(this, category)
        }else if (category is RegularArmour) {
            //propertyMap["CATEGORY"] = AnalysisWeapon(category)
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

    override fun getPowderSpecial(): PowderSpecial? {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun getType(): EquipmentType {
        return parent.getType()
    }

    override fun getLevel(): IRange {
        TODO("Not yet implemented")
    }

    override fun getClassReq(): CharacterClass? {
        TODO("Not yet implemented")
    }

    override fun getQuestReq(): String? {
        TODO("Not yet implemented")
    }

    override fun getRequirement(skill: Skill): Int {
        TODO("Not yet implemented")
    }

    override fun getPowderSlot(): Int {
        TODO("Not yet implemented")
    }

    override fun getRestriction(): Restriction? {
        TODO("Not yet implemented")
    }

    override fun isIdentifiable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun asWeapon(): Weapon? {
        TODO("Not yet implemented")
    }

    override fun asWearable(): Wearable? {
        TODO("Not yet implemented")
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