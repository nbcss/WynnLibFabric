package io.github.nbcss.wynnlib.items.equipments.misc

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.addIdentifications
import io.github.nbcss.wynnlib.items.addItemSuffix
import io.github.nbcss.wynnlib.items.addRequirements
import io.github.nbcss.wynnlib.items.addRestriction
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.JsonGetter
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class Charm(json: JsonObject) : Equipment, EquipmentCategory {
    private val idMap: MutableMap<Identification, BaseIRange> = LinkedHashMap()
    private val spMap: MutableMap<Skill, Int> = LinkedHashMap()
    private val name: String = json["name"].asString
    private val tier: Tier = Tier.fromName(json["tier"].asString)
    private val level: Int = json["level"].asInt
    private val texture: ItemStack
    private val propertyMap: MutableMap<String, String> = mutableMapOf()

    init {
        texture = ItemFactory.fromLegacyId(
            json["material"].asString.split(":")[0].toInt(), json["material"].asString.split(":")[1].toInt()
        )
        if (json.has("properties")) {
            for (entry in json["properties"].asJsonObject.entrySet()) {
                propertyMap[entry.key] = entry.value.asString
            }
        }
        Skill.values()
            .forEach { // There shouldn't be and SP for charm, however I leave it for possible further changes.
                val value = JsonGetter.getOr(json, it.getKey(), 0)
                if (value != 0) {
                    spMap[it] = value
                }
            }
        Identification.getAll().filter { json.has(it.apiId) }.forEach {
            val value = JsonGetter.getOr(json, it.apiId, 0)
            if (value != 0)
                idMap[it] = BaseIRange(it, false, value)
        }
    }

    override fun getType(): EquipmentType = EquipmentType.CHARM

    override fun getIdProperty(key: String): String? {
        return propertyMap[key]
    }

    override fun getTier(): Tier = tier

    override fun getLevel(): IRange = SimpleIRange(level, level)

    override fun getClassReq(): CharacterClass? = null

    override fun getQuestReq(): String? = null

    override fun getRequirement(skill: Skill): Int = 0

    override fun getRestriction(): Restriction = Restriction.SOULBOUND

    override fun isIdentifiable(): Boolean = true

    override fun getKey(): String = name

    override fun getDisplayText(): Text {
        return LiteralText(name).formatted(getTier().formatting)
    }

    override fun getDisplayName(): String = name

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Color = Settings.getTierColor(tier)

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayText())
        tooltip.add(LiteralText.EMPTY)
        addRequirements(this, tooltip)
        tooltip.add(LiteralText.EMPTY)
        if (addIdentifications(this, tooltip))
            tooltip.add(LiteralText.EMPTY)
        addItemSuffix(this, tooltip)
        addRestriction(this, tooltip)
        return tooltip
    }

    override fun getIdentificationRange(id: Identification): IRange {
        return idMap[id] ?: BaseIRange(id, false, 0)
    }
}