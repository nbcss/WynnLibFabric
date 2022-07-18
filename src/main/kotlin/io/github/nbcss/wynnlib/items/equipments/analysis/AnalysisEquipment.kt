package io.github.nbcss.wynnlib.items.equipments.analysis

import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.IdentificationProperty
import io.github.nbcss.wynnlib.items.equipments.analysis.properties.ItemProperty
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class AnalysisEquipment(private val parent: RegularEquipment,
                        tooltip: List<Text>): RolledEquipment {
    companion object {

    }
    private val propertyMap: Map<String, ItemProperty> = mapOf(
        pairs = listOf(
            IdentificationProperty()
        ).map { it.getKey() to it }.toTypedArray()
    )
    private val category: EquipmentCategory?
    init {
        category = null //todo
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

    override fun getIdentificationValue(id: Identification): Int {
        return (propertyMap[IdentificationProperty.KEY] as IdentificationProperty).getIdentificationValue(id)
    }

    override fun getTier(): Tier {
        TODO("Not yet implemented")
    }

    override fun getType(): EquipmentType {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun getDisplayText(): Text {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String {
        TODO("Not yet implemented")
    }

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getRarityColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getIdentificationRange(id: Identification): IRange {
        TODO("Not yet implemented")
    }
}