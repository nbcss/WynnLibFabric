package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class Skill(val id: String,               //The id of the skill; used in equipment req
                 val displayName: String,      //display name
                 val requirementName: String   //The name used in ingredient req
                 ): Keyed, Translatable {
    STRENGTH("strength", "Strength", "strengthRequirement"),
    DEXTERITY("dexterity", "Dexterity", "dexterityRequirement"),
    INTELLIGENCE("intelligence", "Intelligence", "intelligenceRequirement"),
    DEFENCE("defense", "Defence", "defenceRequirement"),
    AGILITY("agility", "Agility", "agilityRequirement");

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = getKey().lowercase(Locale.getDefault())
        if ("tooltip.req" == label){
            return "wynnlib.tooltip.skill_point_req.$key"
        }
        return "wynnlib.tooltip.skill_point.$key"
    }
}