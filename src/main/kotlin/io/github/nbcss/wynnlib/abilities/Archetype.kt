package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.abilities.builder.TreeBuildInfo
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ARCHETYPE_ABILITIES
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.formattingLines
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.collections.LinkedHashMap

enum class Archetype(private val displayName: String,
                     private val color: Formatting,
                     private val character: CharacterClass,
                     meta: Int): Keyed, Translatable {
    //Warrior Archetypes
    FALLEN("Fallen", Formatting.RED, CharacterClass.WARRIOR, 79),
    BATTLE_MONK("Battle Monk", Formatting.YELLOW, CharacterClass.WARRIOR, 78),
    PALADIN("Paladin", Formatting.AQUA, CharacterClass.WARRIOR, 76),
    //Archer Archetypes
    BOLTSLINGER("Boltslinger", Formatting.YELLOW, CharacterClass.ARCHER, 78),
    SHARPSHOOTER("Sharpshooter", Formatting.LIGHT_PURPLE, CharacterClass.ARCHER, 82),
    TRAPPER("Trapper", Formatting.DARK_GREEN, CharacterClass.ARCHER, 81),
    //Mage Archetypes
    RIFTWALKER("Riftwalker", Formatting.AQUA, CharacterClass.MAGE, 76),
    LIGHT_BENDER("Light Bender", Formatting.WHITE, CharacterClass.MAGE, 77),
    ARCANIST("Arcanist", Formatting.DARK_PURPLE, CharacterClass.MAGE, 82),
    //Assassin Archetypes
    SHADESTEPPER("Shadestepper", Formatting.DARK_RED, CharacterClass.ASSASSIN, 79),
    TRICKSTER("Trickster", Formatting.LIGHT_PURPLE, CharacterClass.ASSASSIN, 82),
    ACROBAT("Acrobat", Formatting.WHITE, CharacterClass.ASSASSIN, 77),
    //Shaman Archetypes
    SUMMONER("Summoner", Formatting.GOLD, CharacterClass.SHAMAN, 80),
    RITUALIST("Ritualist", Formatting.GREEN, CharacterClass.SHAMAN, 81),
    ACOLYTE("Acolyte", Formatting.RED, CharacterClass.SHAMAN, 79);
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

    fun getTooltip(build: TreeBuildInfo? = null): List<Text> {
        val tree = AbilityRegistry.fromCharacter(character)
        val tooltip: MutableList<Text> = ArrayList()
        val title = Translations.TOOLTIP_ARCHETYPE_TITLE.translate(null, translate().string)
        tooltip.add(title.formatted(getFormatting()).formatted(Formatting.BOLD))
        tooltip.add(LiteralText.EMPTY)
        formattingLines(translate("desc").string, Formatting.GRAY.toString()).forEach { line ->
            tooltip.add(line)
        }
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(TOOLTIP_ARCHETYPE_ABILITIES.translate(null, tree.getArchetypePoint(this))
            .formatted(Formatting.GRAY))
        tree.getAbilities()
            .filter { it.getArchetype() == this }
            .sortedWith { x, y ->
                if (build != null){
                    val compare = build.hasAbility(x).compareTo(build.hasAbility(y))
                    if (compare != 0)
                        return@sortedWith -compare
                }
                val tier = x.getTier().compareTo(y.getTier())
                return@sortedWith if (tier != 0) tier else
                    x.translate().string.compareTo(y.translate().string)
            }
            .forEach {
                val prefix = if (build == null){
                    LiteralText("- ").formatted(Formatting.GRAY)
                }else if(build.hasAbility(it)){
                    //LiteralText("- ").formatted(Formatting.GREEN)
                    Symbol.TICK.asText().append(" ")
                }else{
                    //LiteralText("- ").formatted(Formatting.RED)
                    Symbol.CROSS.asText().append(" ")
                }
                tooltip.add(prefix.append(it.translate().formatted(it.getTier().getFormatting())))
            }
        return tooltip
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        if (label == "desc"){
            return "wynnlib.archetype.desc.${name.lowercase()}"
        }
        return "wynnlib.archetype.name.${name.lowercase()}"
    }
}