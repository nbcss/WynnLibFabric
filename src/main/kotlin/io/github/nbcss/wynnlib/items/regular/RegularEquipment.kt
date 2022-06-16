package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.Wearable
import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.utils.IRange
import io.github.nbcss.wynnlib.utils.asIdentificationRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularEquipment(json: JsonObject) : Equipment {
    private val idMap: MutableMap<Identification, Int> = LinkedHashMap()
    private val spMap: MutableMap<Skill, Int> = LinkedHashMap()
    private val name: String
    private val displayName: String
    private val classReq: CharacterClass?
    private val questReq: String?
    private val tier: Tier
    private val level: Int
    private val container: EquipmentContainer?
    init {
        name = json.get("name").asString
        displayName = if (json.has("displayName")) json.get("displayName").asString else name
        tier = Tier.getTier(json.get("tier").asString)
        level = json.get("level").asInt
        classReq = null
        questReq = if (json.has("quest") && !json.get("quest").isJsonNull)
            json.get("quest").asString else null
        Skill.values().forEach{
            val value = if (json.has(it.getKey())) json.get(it.getKey()).asInt else 0
            if(value != 0){
                spMap[it] = value
            }
        }
        Metadata.getIdentifications().filter{json.has(it.name)}.forEach{
            val value = json.get(it.name).asInt
            if(value != 0)
                idMap[it] = value
        }
        val category = json.get("category").asString
        container = if(category.equals("weapon")){
            RegularWeapon(this, json)
        }else if(category.equals("armor")){
            RegularArmour(this, json)
        }else if(category.equals("accessory")){
            RegularAccessory(this, json)
        }else{
            null //hmm it should not happen right?
        }
    }

    override fun getTier(): Tier = tier

    override fun getIdentification(id: Identification): IRange {
        return asIdentificationRange(idMap.getOrDefault(id, 0))
    }

    override fun getType(): EquipmentType {
        return container!!.getType()
    }

    override fun getLevel(): IRange = IRange(level, level)

    override fun getClassReq(): CharacterClass? {
        return if(container is Weapon) CharacterClass.fromWeaponType(getType()) else classReq
    }

    override fun getQuestReq(): String? = questReq

    override fun getRequirement(skill: Skill): Int {
        return spMap.getOrDefault(skill, 0)
    }

    override fun getKey(): String = name

    fun getDisplayName(): String = displayName

    override fun getDisplayText(): Text {
        return LiteralText(displayName).formatted(tier.prefix)
    }

    override fun getIcon(): ItemStack = container!!.getIcon()

    override fun getRarityColor(): Int {
        return Settings.getColor("tier_" + tier.name)
    }

    override fun getTooltip(): List<Text> = container!!.getTooltip()

    override fun asWeapon(): Weapon? {
        return if(container is Weapon) container else null
    }

    override fun asWearable(): Wearable? {
        return if(container is Wearable) container else null
    }
}