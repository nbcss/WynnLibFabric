package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import kotlin.math.max
import kotlin.math.min

class Recipe(json: JsonObject): Keyed, BaseItem {
    private val id: String = json["id"].asString
    private val type: CraftedType
    private val profession: Profession
    private val level: IRange
    private val materials: Map<String, Int>
    init {
        val minLv = json["level"].asJsonObject["minimum"].asInt
        val maxLv = json["level"].asJsonObject["maximum"].asInt
        level = SimpleIRange(min(minLv, maxLv), max(minLv, maxLv))
        profession = Profession.fromName(json["skill"].asString) ?: Profession.ARMOURING
        type = CraftedType.fromId(json["type"].asString) ?: CraftedType.BOOTS //LOL
        materials = linkedMapOf(
            pairs = json["materials"].asJsonArray.map { it.asJsonObject }
                .map { it["item"].asString to it["amount"].asInt }.toTypedArray()
        )
    }

    fun getMaterials(): List<Pair<String, Int>> = materials.entries.map { it.key to it.value }

    fun getType(): CraftedType = type

    fun getLevel(): IRange = level

    override fun getDisplayText(): Text {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String {
        TODO("Not yet implemented")
    }

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getRarityColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getKey(): String = id
}