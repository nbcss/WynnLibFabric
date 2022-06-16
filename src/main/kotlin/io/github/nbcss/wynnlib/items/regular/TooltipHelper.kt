package io.github.nbcss.wynnlib.items.regular

import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.lang.Translator
import io.github.nbcss.wynnlib.utils.translate
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun addRequirements(item: Equipment, tooltip: MutableList<Text>) {
    //append level req
    val level = item.getLevel()
    val levelText = LiteralText(": " + if (level.isConstant()) level.start.toString()
        else level.start.toString() + "-" + level.end.toString()).formatted(Formatting.GRAY)
    tooltip.add(translate("wynnlib.tooltip.combat_level_req").formatted(Formatting.GRAY).append(levelText))
    //append skill point req
    Skill.values().forEach{
        val point = item.getRequirement(it)
        if(point != 0){
            val text = LiteralText(": $point").formatted(Formatting.GRAY)
            val prefix = Translator.asText("skill", it.getKey(), "tooltip.req")
            tooltip.add(prefix.formatted(Formatting.GRAY).append(text))
        }
    }
}