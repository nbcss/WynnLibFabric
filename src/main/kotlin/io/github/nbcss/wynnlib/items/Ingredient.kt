package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.Profession
import io.github.nbcss.wynnlib.data.Restriction
import io.github.nbcss.wynnlib.lang.Translatable.Companion.from
import io.github.nbcss.wynnlib.utils.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.getItemById
import io.github.nbcss.wynnlib.utils.getSkullItem
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.IngredientIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper


class Ingredient(json: JsonObject) : Keyed, BaseItem, IdentificationHolder {
    private val idMap: MutableMap<Identification, IngredientIRange> = LinkedHashMap()
    private val professions: MutableList<Profession> = ArrayList()
    private val name: String
    private val displayName: String
    private val level: Int
    private val tier: Tier
    private val untradable: Boolean
    private val texture: ItemStack
    init {
        name = json.get("name").asString
        displayName = if (json.has("displayName")) json.get("displayName").asString else name
        level = json.get("level").asInt
        tier = Tier.values()[MathHelper.clamp(json.get("tier").asInt, 0, Tier.values().size - 1)]
        untradable = json.has("untradeable") && json.get("untradeable").asBoolean
        if (json.has("skills")) {
            json.get("skills").asJsonArray.forEach {
                Profession.getProfession(it.asString)?.let { i -> professions.add(i) }
            }
        }
        if(json.has("identifications")){
            val identifications = json.get("identifications").asJsonObject;
            identifications.entrySet().forEach {
                val id = Identification.fromName(it.key)
                if (id != null){
                    val range = it.value.asJsonObject
                    val min = if (range.has("minimum")) range.get("minimum").asInt else 0
                    val max = if (range.has("maximum")) range.get("maximum").asInt else 0
                    idMap[id] = IngredientIRange(min, max)
                }else{
                    println("Unknown ID Name: ${it.key}")
                }
            }
        }
        texture = if (json.has("skin")) {
            val skin: String = json.get("skin").asString
            getSkullItem(skin)
        } else if (json.has("sprite")) {
            val sprite: JsonObject = json.get("sprite").asJsonObject
            val id = sprite["id"].asInt
            val meta = if (sprite.has("damage")) sprite["damage"].asInt else 0
            getItemById(id, meta)
        } else {
            ERROR_ITEM
        }
    }

    fun getProfessions(): List<Profession> = professions

    fun isUntradable(): Boolean = untradable

    override fun getDisplayText(): Text {
        return LiteralText(displayName).formatted(Formatting.GRAY).append(LiteralText(tier.suffix))
    }

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Int {
        return Settings.getColor("ingredient_tier", tier.name)
    }

    override fun getIdentification(id: Identification): IRange {
        return idMap.getOrDefault(id, IRange.ZERO)
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayText())
        tooltip.add(from("wynnlib.tooltip.crafting_ingredient").translate().formatted(Formatting.DARK_GRAY))
        tooltip.add(LiteralText(""))
        //append empty line if success add any id into the tooltip
        if (addIdentifications(this, tooltip))
            tooltip.add(LiteralText(""))
        //todo
        tooltip.add(from("wynnlib.tooltip.crafting_level_req").translate().formatted(Formatting.GRAY)
            .append(LiteralText(": $level").formatted(Formatting.GRAY)))
        professions.forEach {
            tooltip.add(LiteralText(" - ").formatted(Formatting.DARK_GRAY).append(it.getDisplayText()))
        }
        if(isUntradable())
            tooltip.add(Restriction.UNTRADABLE.translate().formatted(Formatting.RED))
        return tooltip
    }

    override fun getKey(): String = name

    enum class Tier(val suffix: String) {
        STAR_0("§7 [§8✫✫✫§7]"),
        STAR_1("§6 [§e✫§8✫✫§6]"),
        STAR_2("§5 [§d✫✫§8✫§5]"),
        STAR_3("§3 [§b✫✫✫§3]");
    }
}