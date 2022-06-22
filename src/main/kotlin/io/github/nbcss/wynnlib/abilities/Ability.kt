package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.lang.Translations.TOOLTIP_ABILITY_BLOCKS
import io.github.nbcss.wynnlib.lang.Translations.TOOLTIP_ABILITY_DEPENDENCY
import io.github.nbcss.wynnlib.lang.Translations.TOOLTIP_ABILITY_MIN_ARCHETYPE
import io.github.nbcss.wynnlib.lang.Translations.TOOLTIP_ABILITY_POINTS
import io.github.nbcss.wynnlib.lang.Translations.TOOLTIP_ARCHETYPE_TITLE
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.parseStyle
import io.github.nbcss.wynnlib.utils.warpLines
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.CharacterVisitor
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper

class Ability(json: JsonObject): Keyed, Translatable {
    private val id: String
    //private val name: String
    private val tier: Tier
    private val character: CharacterClass
    private val archetype: Archetype?
    private val height: Int
    private val position: Int
    private val cost: Int
    private val dependency: String?
    private val blocks: MutableSet<String> = HashSet()
    private val predecessors: MutableSet<String> = HashSet()
    private val archetypeReq: MutableMap<Archetype, Int> = LinkedHashMap()
    init {
        id = json["id"].asString
        //name = json["name"].asString
        character = CharacterClass.valueOf(json["class"].asString.uppercase())
        archetype = if (json.has("archetype") && !json["archetype"].isJsonNull)
            Archetype.fromName(json["archetype"].asString) else null
        height = json["height"].asInt
        position = json["position"].asInt
        cost = json["cost"].asInt
        dependency = if (json.has("dependency") && !json["dependency"].isJsonNull)
            json["dependency"].asString else null
        blocks.addAll(json["blocks"].asJsonArray.map { e -> e.asString })
        predecessors.addAll(json["predecessors"].asJsonArray.map { e -> e.asString })
        if (json.has("archetype_requirements")){
            val requirements = json["archetype_requirements"].asJsonObject
            requirements.entrySet().forEach {
                Archetype.fromName(it.key)?.let { arch -> archetypeReq[arch] = it.value.asInt }
            }
        }
        val level = MathHelper.clamp(json["tier"].asInt, 0, 4)
        tier = when (level) {
            1 -> Tier.TIER_1
            2 -> Tier.TIER_2
            3 -> Tier.TIER_3
            4 -> Tier.TIER_4
            else -> when (character) {
                CharacterClass.WARRIOR -> Tier.WARRIOR_SPELL
                CharacterClass.ARCHER -> Tier.ARCHER_SPELL
                CharacterClass.MAGE -> Tier.MAGE_SPELL
                CharacterClass.ASSASSIN -> Tier.ASSASSIN_SPELL
                CharacterClass.SHAMAN -> Tier.SHAMAN_SPELL
            }
        }
    }

    fun getCharacter(): CharacterClass = character

    fun getTier(): Tier = tier

    fun getArchetype(): Archetype? = archetype

    fun getHeight(): Int = height

    fun getPosition(): Int = position

    fun getAbilityPointCost(): Int = cost

    fun getPredecessors(): List<Ability> = predecessors.mapNotNull { x -> AbilityRegistry.get(x) }

    fun getBlockAbilities(): List<Ability> = blocks.mapNotNull { x -> AbilityRegistry.get(x) }

    fun getArchetypeRequirement(archetype: Archetype): Int {
        return archetypeReq.getOrDefault(archetype, 0)
    }

    fun getAbilityDependency(): Ability? {
        return if (dependency == null) null else AbilityRegistry.get(dependency)
    }

    fun getTooltip(): List<Text> {
        val tree = AbilityRegistry.fromCharacter(getCharacter())
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(translate().formatted(tier.getFormatting()).formatted(Formatting.BOLD))
        tooltip.add(LiteralText.EMPTY)
        translate("desc").string.split("//").forEach {
            if(it == "") {
                tooltip.add(LiteralText.EMPTY)
            }else{
                warpLines(parseStyle(it, Formatting.GRAY.toString()), 190).forEach { line ->
                    tooltip.add(line)
                }
            }
        }
        tooltip.add(LiteralText.EMPTY)
        //todo effects
        val incompatibles = getBlockAbilities()
        if (incompatibles.isNotEmpty()){
            tooltip.add(TOOLTIP_ABILITY_BLOCKS.translate().formatted(Formatting.RED))
            incompatibles.forEach {
                tooltip.add(LiteralText("- ").formatted(Formatting.RED)
                    .append(it.translate().formatted(Formatting.GRAY)))
            }
            tooltip.add(LiteralText.EMPTY)
        }
        //Requirements
        tooltip.add(TOOLTIP_ABILITY_POINTS.translate().formatted(Formatting.GRAY)
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(LiteralText(getAbilityPointCost().toString()).formatted(Formatting.WHITE)))
        if (dependency != null){
            getAbilityDependency()?.let {
                tooltip.add(TOOLTIP_ABILITY_DEPENDENCY.translate().formatted(Formatting.GRAY)
                    .append(LiteralText(": ").formatted(Formatting.GRAY))
                    .append(it.translate().formatted(Formatting.WHITE)))
            }
        }
        tree.getArchetypes().filter { getArchetypeRequirement(it) != 0 }.forEach {
            val points = getArchetypeRequirement(it).toString()
            val title = TOOLTIP_ABILITY_MIN_ARCHETYPE.translate(null, it.translate().string)
            tooltip.add(title.formatted(Formatting.GRAY)
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(LiteralText(points).formatted(Formatting.WHITE)))
        }
        getArchetype()?.let {
            tooltip.add(LiteralText.EMPTY)
            val title = TOOLTIP_ARCHETYPE_TITLE.translate(null, it.translate().string)
            tooltip.add(title.formatted(it.getFormatting()).formatted(Formatting.BOLD))
        }
        return tooltip
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = id.lowercase()
        if (label == "desc"){
            return "wynnlib.ability.desc.$key"
        }
        return "wynnlib.ability.name.$key"
    }

    enum class Tier(private val level: Int,
                    private val formatting: Formatting,
                    private val locked: ItemStack,
                    private val unlocked: ItemStack,
                    private val active: ItemStack) {
        WARRIOR_SPELL(0, Formatting.GREEN,
            ItemFactory.fromEncoding("minecraft:stone_axe#57"),
            ItemFactory.fromEncoding("minecraft:stone_axe#58"),
            ItemFactory.fromEncoding("minecraft:stone_axe#59")),
        ARCHER_SPELL(0, Formatting.GREEN,
            ItemFactory.fromEncoding("minecraft:stone_axe#60"),
            ItemFactory.fromEncoding("minecraft:stone_axe#61"),
            ItemFactory.fromEncoding("minecraft:stone_axe#62")),
        MAGE_SPELL(0, Formatting.GREEN,
            ItemFactory.fromEncoding("minecraft:stone_axe#66"),
            ItemFactory.fromEncoding("minecraft:stone_axe#67"),
            ItemFactory.fromEncoding("minecraft:stone_axe#68")),
        ASSASSIN_SPELL(0, Formatting.GREEN,
            ItemFactory.fromEncoding("minecraft:stone_axe#63"),
            ItemFactory.fromEncoding("minecraft:stone_axe#64"),
            ItemFactory.fromEncoding("minecraft:stone_axe#65")),
        SHAMAN_SPELL(0, Formatting.GREEN,
            ItemFactory.fromEncoding("minecraft:stone_axe#69"),
            ItemFactory.fromEncoding("minecraft:stone_axe#70"),
            ItemFactory.fromEncoding("minecraft:stone_axe#71")),
        TIER_1(1, Formatting.WHITE,
            ItemFactory.fromEncoding("minecraft:stone_axe#45"),
            ItemFactory.fromEncoding("minecraft:stone_axe#46"),
            ItemFactory.fromEncoding("minecraft:stone_axe#47")),
        TIER_2(2, Formatting.GOLD,
            ItemFactory.fromEncoding("minecraft:stone_axe#48"),
            ItemFactory.fromEncoding("minecraft:stone_axe#49"),
            ItemFactory.fromEncoding("minecraft:stone_axe#50")),
        TIER_3(3, Formatting.LIGHT_PURPLE,
            ItemFactory.fromEncoding("minecraft:stone_axe#51"),
            ItemFactory.fromEncoding("minecraft:stone_axe#52"),
            ItemFactory.fromEncoding("minecraft:stone_axe#53")),
        TIER_4(4, Formatting.RED,
            ItemFactory.fromEncoding("minecraft:stone_axe#54"),
            ItemFactory.fromEncoding("minecraft:stone_axe#55"),
            ItemFactory.fromEncoding("minecraft:stone_axe#56"));

        fun getLevel(): Int = level

        fun getFormatting(): Formatting = formatting

        fun getLockedTexture(): ItemStack = locked

        fun getUnlockedTexture(): ItemStack = unlocked

        fun getActiveTexture(): ItemStack = active
    }
}