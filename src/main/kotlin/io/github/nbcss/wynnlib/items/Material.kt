package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class Material(private val tier: Tier, json: JsonObject) : Keyed, BaseItem {
    private val name: String
    private val displayName: String
    private val type: Type
    private val level: Int
    private val texture: ItemStack
    init {
        name = json["name"].asString
        displayName = json["displayName"].asString
        type = Type.valueOf(json["type"].asString.uppercase())
        level = json["level"].asInt
        texture = ItemFactory.fromEncoding(json["texture"].asString)
    }

    override fun getDisplayText(): Text {
        return LiteralText(displayName).append(tier.suffix)
    }

    override fun getDisplayName(): String = displayName

    override fun getRarityColor(): Color {
        return Settings.getColor("material_tier", tier.name)
    }

    override fun getIcon(): ItemStack = texture

    override fun getKey(): String = name

    enum class Tier(val suffix: String, val coefficient: Double) {
        STAR_1("§6 [§e✫§8✫✫§6]", 1.0),
        STAR_2("§6 [§e✫✫§8✫§6]", 1.25),
        STAR_3("§6 [§e✫✫✫§6]", 1.4);
    }

    enum class Type {
        INGOT,
        GEM,
        WOOD,
        PAPER,
        STRING,
        GRAINS,
        OIL,
        MEAT
    }
}