package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularEquipment(json: JsonObject) : Equipment {
    private val idMap: MutableMap<Identification, BaseIRange> = LinkedHashMap()
    private val spMap: MutableMap<Skill, Int> = LinkedHashMap()
    private val name: String
    private val displayName: String
    private val classReq: CharacterClass?
    private val questReq: String?
    private val restriction: Restriction?
    private val tier: Tier
    private val level: Int
    private val powderSlots: Int
    private val category: EquipmentCategory?
    private val identified: Boolean
    private val majorIds: Array<MajorId>
    init {
        name = json.get("name").asString
        displayName = if (json.has("displayName")) json.get("displayName").asString else name
        tier = Tier.fromName(json.get("tier").asString)
        level = json.get("level").asInt
        classReq = null
        questReq = if (json.has("quest") && !json.get("quest").isJsonNull)
            json.get("quest").asString else null
        restriction = if (json.has("restrictions") && !json.get("restrictions").isJsonNull)
            Restriction.fromId(json.get("restrictions").asString) else null
        powderSlots = if (json.has("sockets")) json.get("sockets").asInt else 0
        identified = json.has("identified") && json.get("identified").asBoolean
        majorIds = if (json.has("majorIds")){
            json["majorIds"].asJsonArray.mapNotNull { MajorId.get(it.asString) }.toTypedArray()
        }else{
            emptyArray()
        }
        Skill.values().forEach{
            val value = if (json.has(it.getKey())) json.get(it.getKey()).asInt else 0
            if(value != 0){
                spMap[it] = value
            }
        }
        Identification.getAll().filter{json.has(it.apiId)}.forEach{
            val value = json.get(it.apiId).asInt
            if(value != 0)
                idMap[it] = BaseIRange(it, value)
        }
        val category = json.get("category").asString
        this.category = if(category.equals("weapon")){
            RegularWeapon(this, json)
        }else if(category.equals("armor")){
            RegularArmour(this, json)
        }else if(category.equals("accessory")){
            RegularAccessory(this, json)
        }else{
            null //hmm it should not happen right? ok it can happen if wynn updates...
        }
    }

    fun getCategory(): EquipmentCategory? = category

    override fun getTier(): Tier = tier

    override fun getIdentificationRange(id: Identification): IRange {
        return idMap.getOrDefault(id, IRange.ZERO)
    }

    override fun getType(): EquipmentType {
        return category?.getType() ?: EquipmentType.INVALID
    }

    override fun getLevel(): IRange = SimpleIRange(level, level)

    override fun getClassReq(): CharacterClass? {
        return if(category is Weapon) CharacterClass.fromWeaponType(getType()) else classReq
    }

    override fun getQuestReq(): String? = questReq

    override fun getRequirement(skill: Skill): Int {
        return spMap.getOrDefault(skill, 0)
    }

    override fun getKey(): String = name

    override fun getDisplayName(): String = displayName

    override fun getDisplayText(): Text {
        return LiteralText(displayName).formatted(tier.formatting)
    }

    override fun getIcon(): ItemStack = category?.getIcon() ?: ERROR_ITEM

    override fun getRarityColor(): Color {
        return Settings.getTierColor(tier)
    }

    override fun getTooltip(): List<Text> = category?.getTooltip() ?: listOf(getDisplayText())

    override fun getPowderSlot(): Int = powderSlots

    override fun getRestriction(): Restriction? = restriction

    override fun isIdentifiable(): Boolean = getTier().canIdentify() && !identified && idMap.isNotEmpty()

    override fun asWeapon(): Weapon? {
        return if(category is Weapon) category else null
    }

    override fun asWearable(): Wearable? {
        return if(category is Wearable) category else null
    }
}