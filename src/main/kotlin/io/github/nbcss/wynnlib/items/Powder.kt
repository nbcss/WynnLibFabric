package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class Powder(json: JsonObject) : Keyed, BaseItem {
    private val name: String
    private val displayName: String
    private val tier: Tier
    private val element: Element
    private val oppoElem: Element
    private val texture: ItemStack
    init {
        name = json["name"].asString
        displayName = json["displayName"].asString
        tier = Tier.values()[MathHelper.clamp(json["tier"].asInt - 1, 0, Tier.values().size - 1)]
        element = Element.fromId(json["element"].asString)!!
        oppoElem = Element.fromId(json["oppositeElement"].asString)!!
        texture = getItem(json["texture"].asString)
    }

    override fun getKey(): String = name

    override fun getDisplayText(): Text = LiteralText(displayName)

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Int {
        return Settings.getColor("powder_tier", tier.name)
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayText())
        //todo
        return tooltip
    }

    enum class Tier {
        I,
        II,
        III,
        IV,
        V,
        VI;
        fun index(): Int = ordinal + 1
    }
}