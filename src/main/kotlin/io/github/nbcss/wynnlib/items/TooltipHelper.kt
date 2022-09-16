package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.analysis.calculator.QualityCalculator
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.IdentificationGroup
import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.i18n.SuffixTranslation
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_CLASS_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_COMBAT_LV_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SLOTS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_QUEST_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_SKILL_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_TO
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.GearEquipment
import io.github.nbcss.wynnlib.items.equipments.RolledEquipment
import io.github.nbcss.wynnlib.items.identity.IdentificationHolder
import io.github.nbcss.wynnlib.utils.*
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun addRequirements(item: Equipment, tooltip: MutableList<Text>) {
    //append class & quest req
    if (item.getClassReq() != null){
        val classReq = item.getClassReq()!!.translate().formatted(Formatting.GRAY)
        val prefix = TOOLTIP_CLASS_REQ.formatted(Formatting.GRAY)
        tooltip.add(prefix.append(LiteralText(": ").formatted(Formatting.GRAY)).append(classReq))
    }
    if (item.getQuestReq() != null){
        val quest = LiteralText(": ${item.getQuestReq()}").formatted(Formatting.GRAY)
        val prefix = TOOLTIP_QUEST_REQ.formatted(Formatting.GRAY)
        tooltip.add(prefix.append(quest))
    }
    //append level req
    val level = item.getLevel()
    val levelText = LiteralText(": " + if (level.isConstant()) level.lower().toString()
        else level.lower().toString() + "-" + level.upper().toString()).formatted(Formatting.GRAY)
    tooltip.add(TOOLTIP_COMBAT_LV_REQ.formatted(Formatting.GRAY).append(levelText))
    //append skill point req
    Skill.values().forEach{
        val point = item.getRequirement(it)
        if(point != 0){
            val text = LiteralText(": $point").formatted(Formatting.GRAY)
            val prefix = TOOLTIP_SKILL_REQ.translate(null, it.translate().string)
            tooltip.add(prefix.formatted(Formatting.GRAY).append(text))
        }
    }
}

fun addRolledRequirements(item: RolledEquipment, tooltip: MutableList<Text>) {
    //append class & quest req
    if (item.getClassReq() != null){
        val classReq = item.getClassReq()!!.translate().formatted(Formatting.GRAY)
        val name = TOOLTIP_CLASS_REQ.formatted(Formatting.GRAY)
        val prefix = if (item.meetClassReq()) Symbol.TICK.asText() else Symbol.CROSS.asText()
        tooltip.add(prefix.append(" ").append(name.append(": ").append(classReq)))
    }
    if (item.getQuestReq() != null){
        val quest = LiteralText(": ${item.getQuestReq()}").formatted(Formatting.GRAY)
        val name = TOOLTIP_QUEST_REQ.formatted(Formatting.GRAY)
        val prefix = if (item.meetQuestReq()) Symbol.TICK.asText() else Symbol.CROSS.asText()
        tooltip.add(prefix.append(" ").append(name.append(quest)))
    }
    //append level req
    run {
        val level = item.getLevel()
        val levelText = LiteralText(": " + if (level.isConstant()) level.lower().toString()
        else level.lower().toString() + "-" + level.upper().toString()).formatted(Formatting.GRAY)
        val prefix = if (item.meetLevelReq()) Symbol.TICK.asText() else Symbol.CROSS.asText()
        tooltip.add(prefix.append(" ").append(TOOLTIP_COMBAT_LV_REQ.formatted(Formatting.GRAY).append(levelText)))
    }
    //append skill point req
    Skill.values().forEach{
        val point = item.getRequirement(it)
        if(point != 0){
            val text = LiteralText(": $point").formatted(Formatting.GRAY)
            val name = TOOLTIP_SKILL_REQ.translate(null, it.translate().string)
            val prefix = if (item.meetSkillReq(it)) Symbol.TICK.asText() else Symbol.CROSS.asText()
            tooltip.add(prefix.append(" ").append(name.formatted(Formatting.GRAY).append(text)))
        }
    }
}

fun addIdentifications(item: IdentificationHolder,
                       tooltip: MutableList<Text>): Boolean {
    val lastSize = tooltip.size
    var lastGroup: IdentificationGroup? = null
    Identification.getAll().forEach {
        val range = item.getIdentificationRange(it)
        if (!range.isZero()){
            val color = colorOf(if (it.inverted) -range.lower() else range.lower())
            val text = SuffixTranslation.withSuffix(range.lower(), it.suffix).formatted(color)
            if (!range.isConstant()){
                val nextColor = colorOf(if (it.inverted) -range.upper() else range.upper())
                val rangeColor = colorOfDark(
                    when {
                        color != nextColor -> 0
                        it.inverted -> -range.lower()
                        else -> range.lower()
                    }
                )
                text.append(TOOLTIP_TO.formatted(rangeColor))
                text.append(SuffixTranslation.withSuffix(range.upper(), it.suffix).formatted(nextColor))
            }
            if (lastGroup != null && lastGroup != it.group)
                tooltip.add(LiteralText.EMPTY)
            lastGroup = it.group
            tooltip.add(text.append(" ").append(it.getDisplayText(Formatting.GRAY, item)))
        }
    }
    val majorIds = item.getMajorIds()
    if (majorIds.isNotEmpty() && tooltip.size > lastSize)
        tooltip.add(LiteralText.EMPTY)
    for (majorId in majorIds) {
        val text = LiteralText("+")
            .append(majorId.formatted(Formatting.AQUA))
            .append(":ÀÀ")
            .append(majorId.formatted(Formatting.DARK_AQUA, "desc")).string
        tooltip.addAll(warpLines(LiteralText(text).formatted(Formatting.AQUA), 190))
    }
    return tooltip.size > lastSize
}

fun addPowderSpecial(item: RolledEquipment, tooltip: MutableList<Text>) {
    item.getPowderSpecial()?.let {
        tooltip.addAll(it.getTooltip().map { line -> LiteralText("   ").append(line) })
    }
}

fun addRolledIdentifications(item: RolledEquipment,
                             tooltip: MutableList<Text>): Float? {
    val qualities: MutableList<Float> = mutableListOf()
    var lastGroup: IdentificationGroup? = null
    Identification.getAll().forEach {
        val value = item.getIdentificationValue(it)
        if (value != 0){
            val color = colorOf(if (it.inverted) -value else value)
            val text = SuffixTranslation.withSuffix(value, it.suffix).formatted(color)
            val stars = item.getIdentificationStars(it)
            if (stars > 0) {
                text.append(LiteralText(MutableList(stars){ "*" }.reduce { x, y -> x + y})
                    .formatted(Formatting.DARK_GREEN))
            }
            if (lastGroup != null && lastGroup != it.group)
                tooltip.add(LiteralText.EMPTY)
            lastGroup = it.group
            text.append(" ").append(it.getDisplayText(Formatting.GRAY, item))
            val range = item.getIdentificationRange(it) as BaseIRange
            val quality = QualityCalculator.asQuality(value, stars, range)
            quality.second?.let { q -> qualities.add(q) }
            tooltip.add(text.append(" ").append(quality.first))
        }
    }
    return if (qualities.isEmpty()) null else qualities.average().toFloat()
}

fun addRolledPowderSlots(item: RolledEquipment, tooltip: MutableList<Text>) {
    if (item.getPowderSlot() == 0)
        return
    val powders = item.getPowders()
    val text = LiteralText("[${powders.size}/${item.getPowderSlot()}] ").formatted(Formatting.GRAY)
        .append(TOOLTIP_POWDER_SLOTS.formatted(Formatting.GRAY))
    if (powders.isNotEmpty()) {
        text.append(LiteralText(" [").formatted(Formatting.GRAY))
            .append(LiteralText(powders[0].icon).formatted(powders[0].color))
        for (i in (1 until powders.size)) {
            text.append(" ").append(LiteralText(powders[i].icon).formatted(powders[i].color))
        }
        text.append(LiteralText("]").formatted(Formatting.GRAY))
    }
    tooltip.add(text)
}

fun addPowderSlots(item: GearEquipment, tooltip: MutableList<Text>) {
    val slots = LiteralText(item.getPowderSlot().toString()).formatted(
        if (item.getPowderSlot() >= 2) Formatting.GREEN else if
                (item.getPowderSlot() > 0) Formatting.YELLOW else Formatting.RED)
    tooltip.add(LiteralText("[").formatted(Formatting.GRAY)
        .append(slots).append(LiteralText("] ").formatted(Formatting.GRAY))
        .append(TOOLTIP_POWDER_SLOTS.translate().formatted(Formatting.GRAY)))
}

fun addItemSuffix(item: Equipment, tooltip: MutableList<Text>, roll: Int = 0) {
    val tier = item.getTier().translate().formatted(item.getTier().formatting)
    val type = item.getType().translate().formatted(item.getTier().formatting)
    val text = tier.append(LiteralText(" ").append(type))
    if(item.isIdentifiable()){
        var cost = item.getTier().getIdentifyPrice(item.getLevel().lower())
        for (i in (0 until roll)){
            cost *= 5
        }
        if (roll > 1) {
            text.append(" [$roll]")
        }
        text.append(LiteralText(" [" + formatNumbers(cost) + "\u00B2]").formatted(Formatting.GREEN))
    }
    tooltip.add(text)
}

fun addRestriction(item: Equipment, tooltip: MutableList<Text>) {
    if (item.getRestriction() != null){
        tooltip.add(item.getRestriction()!!.translate().formatted(Formatting.RED))
    }
}