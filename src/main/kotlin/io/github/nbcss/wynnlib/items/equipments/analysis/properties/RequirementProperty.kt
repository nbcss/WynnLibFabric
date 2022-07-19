package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.Text
import java.util.regex.Pattern

class RequirementProperty: ItemProperty {
    companion object {
        private val LEVEL_PATTERN = Pattern.compile(" Combat Lv\\. Min: (\\d+)")
        private val CHARACTER_PATTERN = Pattern.compile(" Class Req: (.+)")
        private val QUEST_PATTERN = Pattern.compile(" Quest Req: (.+)")
        private val SKILL_PATTERN = Pattern.compile(" (.+) Min: (\\d+)")
        const val KEY = "REQUIREMENT"
    }
    private val reqMap: MutableMap<Skill, Int> = mutableMapOf()
    private var reqFlagMap: MutableMap<Skill, Boolean> = mutableMapOf()
    private var level: Int = 0
    private var levelFlag: Boolean = true
    private var character: CharacterClass? = null
    private var characterFlag: Boolean = true
    private var quest: String? = null
    private var questFlag: Boolean = true

    fun getLevel(): Int = level

    fun meetLevelReq(): Boolean = levelFlag

    fun getClassReq(): CharacterClass? = character

    fun meetClassReq(): Boolean = characterFlag

    fun getQuestReq(): String? = quest

    fun meetQuestReq(): Boolean = questFlag

    fun getRequirement(skill: Skill): Int = reqMap.getOrDefault(skill, 0)

    fun meetSkillReq(skill: Skill): Boolean = reqFlagMap.getOrDefault(skill, true)

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if (base.asString() != "" || base.siblings.size != 2)
            return 0
        val text = base.siblings[1].asString()
        val flag = base.siblings[0].asString() == Symbol.TICK.icon
        run {
            val matcher = LEVEL_PATTERN.matcher(text)
            if (matcher.find()) {
                level = matcher.group(1).toInt()
                levelFlag = flag
                return 1
            }
        }
        run {
            val matcher = CHARACTER_PATTERN.matcher(text)
            if (matcher.find()) {
                CharacterClass.fromDisplayName(matcher.group(1))?.let {
                    character = it
                    characterFlag = flag
                    return 1
                }
            }
        }
        run {
            val matcher = QUEST_PATTERN.matcher(text)
            if (matcher.find()) {
                quest = matcher.group(1)
                questFlag = flag
                return 1
            }
        }
        run {
            val matcher = SKILL_PATTERN.matcher(text)
            if (matcher.find()) {
                Skill.fromDisplayName(matcher.group(1))?.let {
                    reqMap[it] = matcher.group(2).toInt()
                    reqFlagMap[it] = flag
                    return 1
                }
            }
        }
        return 0
    }

    override fun getKey(): String = KEY
}