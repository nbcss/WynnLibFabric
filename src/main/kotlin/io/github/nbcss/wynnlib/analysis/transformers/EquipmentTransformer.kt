package io.github.nbcss.wynnlib.analysis.transformers

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.analysis.TooltipTransformer
import io.github.nbcss.wynnlib.analysis.TransformableItem
import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.analysis.properties.PriceProperty
import io.github.nbcss.wynnlib.analysis.properties.equipment.*
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.items.equipments.regular.RegularEquipment
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class EquipmentTransformer(private val parent: RegularEquipment,
                           private val stack: ItemStack): TooltipTransformer, RolledEquipment {
    companion object: TooltipTransformer.Factory {
        const val KEY = "EQUIPMENT"

        override fun create(stack: ItemStack, item: TransformableItem): TooltipTransformer? {
            if (item is RegularEquipment) {
                return EquipmentTransformer(item, stack)
            }
            return null
        }
        override fun getKey(): String = KEY
    }
    private val propertyMap: MutableMap<String, AnalysisProperty> = mutableMapOf(
        pairs = listOf(
            RequirementProperty(),
            IdentificationProperty(),
            PowderSpecialProperty(),
            PowderProperty(),
            SuffixProperty(),
            RestrictionProperty(),
            PriceProperty(),
        ).map { it.getKey() to it }.toTypedArray()
    )
    init {
        val category = parent.getCategory()
        if (category is Weapon) {
            propertyMap["CATEGORY"] = WeaponProperty(this)
        }else if (category is Wearable) {
            propertyMap["CATEGORY"] = WearableProperty(this)
        }
    }

    override fun init() {
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

    override fun getTooltip(): List<Text> {
        val category = propertyMap["CATEGORY"]
        val tooltip = if (category is TooltipProvider) category.getTooltip() else
            stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
        getPriceTooltip()?.let {
            val mutableTooltip = tooltip.toMutableList()
            mutableTooltip.add(LiteralText.EMPTY)
            mutableTooltip.addAll(it)
            return mutableTooltip
        }
        return tooltip
    }

    override fun getIdProperty(key: String): String? {
        SpellSlot.fromKey(key)?.let { spell ->
            var name = spell.translate().string
            getClassReq()?.let {
                AbilityRegistry.fromCharacter(it).getSpellAbility(spell)?.let { ability ->
                    name = ability.translate().string
                }
            }
            return name
        }
        return null
    }

    private fun getPriceTooltip(): List<Text>? {
        return (propertyMap[PriceProperty.KEY] as PriceProperty).getPriceTooltip()
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
        return (propertyMap[SuffixProperty.KEY] as SuffixProperty).getRoll()
    }

    override fun getIdentificationValue(id: Identification): Int {
        return (propertyMap[IdentificationProperty.KEY] as IdentificationProperty).getIdentificationValue(id)
    }

    override fun getIdentificationStars(id: Identification): Int {
        return (propertyMap[IdentificationProperty.KEY] as IdentificationProperty).getIdentificationStars(id)
    }

    override fun getTier(): Tier {
        return (propertyMap[SuffixProperty.KEY] as SuffixProperty).getTier() ?: parent.getTier()
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
        return (propertyMap[RestrictionProperty.KEY] as RestrictionProperty).getRestriction()
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

    override fun getIdentificationRange(id: Identification): BaseIRange {
        return parent.getIdentificationRange(id)
    }
}