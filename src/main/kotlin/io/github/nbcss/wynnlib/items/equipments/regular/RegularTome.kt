package io.github.nbcss.wynnlib.items.equipments.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.addIdentifications
import io.github.nbcss.wynnlib.items.addItemSuffix
import io.github.nbcss.wynnlib.items.addRestriction
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class RegularTome(json: JsonObject) : Equipment, EquipmentCategory {
    private val idMap: MutableMap<Identification, BaseIRange> = LinkedHashMap()
    private val spMap: MutableMap<Skill, Int> = LinkedHashMap()
    private val name: String = json["name"].asString
    private val tier: Tier = Tier.fromName(json["tier"].asString)
    private val type: TomeType = TomeType.fromId(json["type"].asString)
    private val level: Int = json["level"].asInt
    private val effectBase: Int = json["effectBase"]?.asInt ?: 0
    private val texture: ItemStack = Items.ENCHANTED_BOOK.defaultStack;
    private val identified: Boolean

    init {
        identified = json.has("identified") && json.get("identified").asBoolean
        Skill.values().forEach {
            val value = if (json.has(it.getKey())) json.get(it.getKey()).asInt else 0
            if (value != 0) {
                spMap[it] = value
            }
        }
        Identification.getAll().filter { json.has(it.apiId) }.forEach {
            val value = json.get(it.apiId).asInt
            if (value != 0)
                idMap[it] = BaseIRange(it, identified, value)
        }
    }

    fun getTomeType(): TomeType = type

    override fun getType(): EquipmentType = EquipmentType.TOME


    override fun getTier(): Tier = tier

    override fun getLevel(): IRange = SimpleIRange(level, level)

    override fun getClassReq(): CharacterClass? = null

    override fun getQuestReq(): String? = null

    override fun getRequirement(skill: Skill): Int = 0

    override fun getPowderSlot(): Int = 0

    override fun getRestriction(): Restriction {
        return type.restriction
    }

    override fun isIdentifiable(): Boolean = true

    override fun asWeapon(): Weapon? = null

    override fun asWearable(): Wearable? = null

    override fun getKey(): String = name

    override fun getDisplayText(): Text {
        return LiteralText(name).formatted(getTier().formatting)
    }

    override fun getDisplayName(): String = name

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Color = Settings.getTierColor(tier)

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.addAll(super.getTooltip())
        tooltip.add(LiteralText.EMPTY)
        addIdentifications(this, tooltip, this.getClassReq())
        addItemSuffix(this, tooltip)
        addRestriction(this, tooltip)
        return tooltip
    }

    override fun getIdentificationRange(id: Identification): IRange {
        return idMap[id] ?: BaseIRange(id, identified, 0)
    }
}