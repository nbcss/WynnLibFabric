package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.lang.Translatable.Companion.from
import io.github.nbcss.wynnlib.utils.formatNumbers
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun addRequirements(item: Equipment, tooltip: MutableList<Text>) {
    //append class & quest req
    if (item.getClassReq() != null){
        val classReq = item.getClassReq()!!.translate().formatted(Formatting.GRAY)
        val prefix = from("wynnlib.tooltip.class_req").translate().formatted(Formatting.GRAY)
        tooltip.add(prefix.append(LiteralText(": ").formatted(Formatting.GRAY)).append(classReq))
    }
    if (item.getQuestReq() != null){
        val quest = LiteralText(": " + item.getQuestReq()).formatted(Formatting.GRAY)
        val prefix = from("wynnlib.tooltip.quest_req").translate().formatted(Formatting.GRAY)
        tooltip.add(prefix.append(quest))
    }
    //append level req
    val level = item.getLevel()
    val levelText = LiteralText(": " + if (level.isConstant()) level.start.toString()
        else level.start.toString() + "-" + level.end.toString()).formatted(Formatting.GRAY)
    tooltip.add(from("wynnlib.tooltip.combat_level_req").translate().formatted(Formatting.GRAY).append(levelText))
    //append skill point req
    Skill.values().forEach{
        val point = item.getRequirement(it)
        if(point != 0){
            val text = LiteralText(": $point").formatted(Formatting.GRAY)
            val prefix = it.translate("tooltip.req").formatted(Formatting.GRAY)
            tooltip.add(prefix.append(text))
        }
    }
}

fun addIdentifications(item: Equipment, tooltip: MutableList<Text>): Boolean {
    //TODO
    Metadata.getIdentifications().forEach {
        val range = item.getIdentification(it)
        if (!range.isZero()){

        }
    }
    return false
}

fun addPowderSlots(item: Equipment, tooltip: MutableList<Text>) {
    val slots = LiteralText(item.getPowderSlot().toString()).formatted(
        if (item.getPowderSlot() >= 2) Formatting.GREEN else if
                (item.getPowderSlot() > 0) Formatting.YELLOW else Formatting.RED)
    tooltip.add(LiteralText("[").formatted(Formatting.GRAY)
        .append(slots).append(LiteralText("] ").formatted(Formatting.GRAY))
        .append(from("wynnlib.tooltip.powder_slots").translate().formatted(Formatting.GRAY)))
}

fun addItemSuffix(item: Equipment, tooltip: MutableList<Text>) {
    val tier = item.getTier().translate().formatted(item.getTier().formatting)
    val type = item.getType().translate().formatted(item.getTier().formatting)
    val text = tier.append(LiteralText(" ").append(type))
    if(item.isIdentifiable()){
        val cost = item.getTier().getIdentifyPrice(item.getLevel().start)
        text.append(LiteralText(" [" + formatNumbers(cost) + "\u00B2]").formatted(Formatting.GREEN))
    }
    tooltip.add(text)
}

fun addRestriction(item: Equipment, tooltip: MutableList<Text>) {
    if (item.getRestriction() != null){
        tooltip.add(item.getRestriction()!!.translate().formatted(Formatting.RED))
    }
}