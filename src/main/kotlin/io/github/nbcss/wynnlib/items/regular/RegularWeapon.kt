package io.github.nbcss.wynnlib.items.regular

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.lang.Translator
import io.github.nbcss.wynnlib.utils.IRange
import io.github.nbcss.wynnlib.utils.asRange
import io.github.nbcss.wynnlib.utils.getItemById
import io.github.nbcss.wynnlib.utils.translate
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

class RegularWeapon(private val parent: RegularEquipment, json: JsonObject)
    : Weapon, EquipmentContainer {
    private val elemDamage: MutableMap<Element, IRange> = LinkedHashMap()
    private val type: EquipmentType
    private val damage: IRange
    private val atkSpeed: AttackSpeed
    private val texture: ItemStack

    init {
        type = EquipmentType.getEquipmentType(json.get("type").asString)
        damage = asRange(json.get("damage").asString)
        atkSpeed = AttackSpeed.getAttackSpeed(json.get("attackSpeed").asString)
        Element.values().forEach{elemDamage[it] = asRange(json.get(it.damageName).asString)}
        texture = if (json.has("material") && !json.get("material").isJsonNull) {
            val material: String = json.get("material").asString
            val materials = material.split(":").toTypedArray()
            val meta = if (materials.size > 1) materials[1].toInt() else 0
            getItemById(materials[0].toInt(), meta)
        } else {
            getType().getIcon()
        }
    }

    override fun getDamage(): IRange = damage

    override fun getElementDamage(elem: Element): IRange {
        return elemDamage.getOrDefault(elem, IRange(0, 0))
    }

    override fun getAttackSpeed(): AttackSpeed = atkSpeed

    override fun getType(): EquipmentType = type

    override fun getIcon(): ItemStack {
        return texture
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(parent.getDisplayText())
        tooltip.add(Translator.asText("attack_speed", atkSpeed.getKey()).formatted(Formatting.GRAY))
        tooltip.add(LiteralText(""))
        val lastSize = tooltip.size
        if(!damage.isZero()){
            val text = LiteralText(": " + damage.start.toString() + "-" + damage.end.toString())
            val prefix = translate("wynnlib.tooltip.neutral_damage")
            tooltip.add(prefix.append(text.formatted(Formatting.GOLD)))
        }
        Element.values().forEach {
            val range: IRange = getElementDamage(it)
            if (!range.isZero()) {
                val text = LiteralText(": " + range.start.toString() + "-" + range.end.toString())
                val prefix = Translator.asText("element", it.getKey(), "tooltip.damage")
                tooltip.add(prefix.append(text.formatted(Formatting.GRAY)))
            }
        }
        //append additional one empty line if no damage been added
        if (tooltip.size > lastSize) tooltip.add(LiteralText(""))
        addRequirements(parent, tooltip)
        return tooltip
    }
}