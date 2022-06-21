package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.util.Formatting
import kotlin.collections.LinkedHashMap

enum class Archetype(private val displayName: String,
                     private val color: Formatting,
                     private val character: CharacterClass,
                     meta: Int): Keyed {
    //Warrior Archetypes
    FALLEN("Fallen", Formatting.RED, CharacterClass.WARRIOR, 75),
    BATTLE_MONK("Battle Monk", Formatting.YELLOW, CharacterClass.WARRIOR, 74),
    PALADIN("Paladin", Formatting.AQUA, CharacterClass.WARRIOR, 72),
    //Archer Archetypes
    BOLTSLINGER("Boltslinger", Formatting.YELLOW, CharacterClass.ARCHER, 74),
    SHARPSHOOTER("Sharpshooter", Formatting.LIGHT_PURPLE, CharacterClass.ARCHER, 78),
    TRAPPER("Trapper", Formatting.GREEN, CharacterClass.ARCHER, 77),
    //Mage Archetypes (incomplete)
    RIFTWALKER("Riftwalker", Formatting.WHITE, CharacterClass.MAGE, 72),
    LIGHT_BENDER("Light Bender", Formatting.WHITE, CharacterClass.MAGE, 72),
    ARCANIST("Arcanist", Formatting.WHITE, CharacterClass.MAGE, 72),
    //Assassin Archetypes (incomplete)
    SHADESTEPPER("Shadestepper", Formatting.WHITE, CharacterClass.ASSASSIN, 72),
    TRICKSTER("Trickster", Formatting.WHITE, CharacterClass.ASSASSIN, 72),
    ACROBAT("Acrobat", Formatting.WHITE, CharacterClass.ASSASSIN, 72),
    //Shaman Archetypes (incomplete)
    SUMMONER("Summoner", Formatting.WHITE, CharacterClass.SHAMAN, 72),
    RITUALIST("Ritualist", Formatting.WHITE, CharacterClass.SHAMAN, 72),
    ACOLYTE("Acolyte", Formatting.WHITE, CharacterClass.SHAMAN, 72);
    private val texture: ItemStack = ItemFactory.fromEncoding("minecraft:stone_axe#$meta")
    companion object {
        private val VALUE_MAP: MutableMap<String, Archetype> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.uppercase()] = it }
        }

        fun fromName(name: String): Archetype? = VALUE_MAP[name.uppercase()]
    }

    fun getDisplayName(): String = displayName

    fun getFormatting(): Formatting = color

    fun getIconText(): String = name.substring(0, 1)

    fun getCharacter(): CharacterClass = character

    fun getTexture(): ItemStack = texture

    override fun getKey(): String = name

}