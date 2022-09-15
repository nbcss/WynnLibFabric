package io.github.nbcss.wynnlib.items.equipments.misc

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.i18n.Translatable.Companion.from
import io.github.nbcss.wynnlib.items.addIdentifications
import io.github.nbcss.wynnlib.items.addItemSuffix
import io.github.nbcss.wynnlib.items.addRequirements
import io.github.nbcss.wynnlib.items.addRestriction
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.formattingLines
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Tome(json: JsonObject) : Equipment, EquipmentCategory {
    companion object {
        private val EFFECT = from("wynnlib.tooltip.tome.effect")
    }
    private val idMap: MutableMap<Identification, BaseIRange> = LinkedHashMap()
    private val spMap: MutableMap<Skill, Int> = LinkedHashMap()
    private val name: String = json["name"].asString
    private val tier: Tier = Tier.fromName(json["tier"].asString)
    private val type: TomeType = TomeType.fromId(json["type"].asString)
    private val level: Int = json["level"].asInt
    private val effectBase: Int = json["effectBase"]?.asInt ?: 0
    private val texture: ItemStack = Items.ENCHANTED_BOOK.defaultStack;
    init {
        Skill.values().forEach {
            val value = if (json.has(it.getKey())) json.get(it.getKey()).asInt else 0
            if (value != 0) {
                spMap[it] = value
            }
        }
        Identification.getAll().filter { json.has(it.apiId) }.forEach {
            val value = json.get(it.apiId).asInt
            if (value != 0)
                idMap[it] = BaseIRange(it, false, value)
        }
    }

    fun getTomeType(): TomeType = type

    override fun getType(): EquipmentType = EquipmentType.TOME


    override fun getTier(): Tier = tier

    override fun getLevel(): IRange = SimpleIRange(level, level)

    override fun getClassReq(): CharacterClass? = null

    override fun getQuestReq(): String? = null

    override fun getRequirement(skill: Skill): Int = 0

    override fun getRestriction(): Restriction {
        return type.restriction
    }

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
        val effect = getTomeType().getEffect()
        if (effect != null && effectBase != 0) {
            val id = "${signed(effectBase)}${effect.suffix} ${effect.translate().string}"
            val text = EFFECT.translate(null, id).string
            tooltip.addAll(formattingLines(text, "${Formatting.GRAY}", 500))
            tooltip.add(LiteralText.EMPTY)
        }
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