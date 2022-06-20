package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class Ability(json: JsonObject): Keyed, Translatable {
    private val id: String
    //private val name: String
    private val tier: Int
    private val character: CharacterClass
    private val texture: ItemStack
    private val height: Int
    private val position: Int
    private val predecessors: MutableSet<String> = HashSet()
    init {
        id = json["id"].asString
        //name = json["name"].asString
        character = CharacterClass.valueOf(json["character"].asString.uppercase())
        height = json["height"].asInt
        position = json["position"].asInt
        predecessors.addAll(json["predecessors"].asJsonArray.map { e -> e.asString })
        tier = MathHelper.clamp(json["tier"].asInt, 0, 4)
        texture = when (tier) {
            0 -> when (character) {
                CharacterClass.WARRIOR -> ItemFactory.fromEncoding("minecraft:stone_axe#58")
                CharacterClass.ARCHER -> ItemFactory.fromEncoding("minecraft:stone_axe#61")
                CharacterClass.MAGE -> ItemFactory.fromEncoding("minecraft:stone_axe#67")
                CharacterClass.ASSASSIN -> ItemFactory.fromEncoding("minecraft:stone_axe#64")
                CharacterClass.SHAMAN -> ItemFactory.fromEncoding("minecraft:stone_axe#70")
            }
            1 -> ItemFactory.fromEncoding("minecraft:stone_axe#46")
            2 -> ItemFactory.fromEncoding("minecraft:stone_axe#49")
            3 -> ItemFactory.fromEncoding("minecraft:stone_axe#52")
            4 -> ItemFactory.fromEncoding("minecraft:stone_axe#55")
            else -> ItemFactory.ERROR_ITEM
        }
    }

    fun getCharacter(): CharacterClass = character

    fun getTexture(): ItemStack = texture

    fun getHeight(): Int = height

    fun getPosition(): Int = position

    fun getPredecessors(): List<String> = predecessors.toList()

    fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(LiteralText(getKey()))
        return tooltip
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = id.lowercase()
        if (label == "description"){
            return "wynnlib.ability.description.$key"
        }
        return "wynnlib.ability.$key"
    }
}