package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.builder.AbilityBuild
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.MainAttackEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translatable.Companion.from
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_BLOCKS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_DEPENDENCY
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MIN_ARCHETYPE
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_POINTS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ARCHETYPE_TITLE
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import java.util.function.Supplier
import kotlin.collections.ArrayList

class Ability(json: JsonObject): Keyed, Translatable, PlaceholderContainer, PropertyProvider {
    private val id: String
    private val name: String?
    private val tier: Tier
    private val character: CharacterClass
    private val archetype: Archetype?
    private val height: Int
    private val position: Int
    private val index: Int
    private val page: Int
    private val slot: Int
    private val cost: Int
    private val dependency: String?
    private val blocks: MutableSet<String> = mutableSetOf()
    private val predecessors: MutableSet<String> = mutableSetOf()
    private val archetypeReq: MutableMap<Archetype, Int> = linkedMapOf()
    private val placeholderMap: MutableMap<String, Supplier<String>> = mutableMapOf()
    private val properties: MutableMap<String, AbilityProperty> = linkedMapOf()
    private val metadata: AbilityMetadata?
    private var successors: List<Ability>? = null
    //private val effect: AbilityEffect
    init {
        id = json["id"].asString
        name = if (json.has("name") && !json["name"].isJsonNull) json["name"].asString else null
        character = CharacterClass.valueOf(json["class"].asString.uppercase())
        archetype = if (json.has("archetype") && !json["archetype"].isJsonNull)
            Archetype.fromName(json["archetype"].asString) else null
        height = json["height"].asInt
        position = json["position"].asInt
        cost = json["cost"].asInt
        index = JsonGetter.getOr(json, "index", -1)
        if (json.has("location") && !json["location"].isJsonNull) {
            val loc = json["location"].asString.split(",")
            page = loc[0].toInt()
            slot = loc[1].toInt() * 9 + loc[2].toInt()
        }else{
            page = 0
            slot = 0
        }
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
            else -> Tier.ofCharacter(character)
        }
        metadata = if (json.has("metadata") && json["metadata"].isJsonObject){
            AbilityMetadata(this, json["metadata"].asJsonObject)
        }else{
            null
        }
        if (json.has("properties")){
            json["properties"].asJsonObject.entrySet().forEach {
                val property = AbilityProperty.fromData(this, it.key, it.value)
                if (property != null) {
                    properties[it.key] = property
                    property.writePlaceholder(this)
                }else{
                    println("[Warning] Cannot read ability property ${it.key}")
                }
            }
        }
    }

    fun getName(): String? = name

    fun getCharacter(): CharacterClass = character

    fun isMainAttack(): Boolean = metadata?.getFactory() is MainAttackEntry.Companion

    fun getTier(): Tier = tier

    fun getArchetype(): Archetype? = archetype

    fun getHeight(): Int = height

    fun getPosition(): Int = position

    fun getPage(): Int = page

    fun getSlot(): Int = slot

    fun getAbilityPointCost(): Int = cost

    fun getMetadata(): AbilityMetadata? = metadata

    fun getPredecessors(): List<Ability> = predecessors.mapNotNull { x -> AbilityRegistry.get(x) }

    fun getSuccessors(): List<Ability> {
        if (successors == null) {
            successors = AbilityRegistry.fromCharacter(character)
                .getAbilities().filter { this in it.getPredecessors() }
        }
        return successors!!
    }

    fun getBlockAbilities(): List<Ability> = blocks.mapNotNull { x -> AbilityRegistry.get(x) }

    fun getArchetypeRequirement(archetype: Archetype): Int {
        return archetypeReq.getOrDefault(archetype, 0)
    }

    fun getAbilityDependency(): Ability? {
        return if (dependency == null) null else AbilityRegistry.get(dependency)
    }

    //fun getEffect(): AbilityEffect = effect

    fun getPropertiesTooltip(): List<Text> {
        return properties.values.map { it.getTooltip() }.flatten()
    }

    fun getIndex(): Int = index

    fun getProperties(): List<AbilityProperty> = properties.values.toList()

    override fun getProperty(key: String): AbilityProperty? {
        return properties[key]
    }

    override fun getPlaceholder(key: String): String {
        return placeholderMap[key]?.get() ?: key
    }

    override fun putPlaceholder(key: String, value: Supplier<String>) {
        placeholderMap[key] = value
    }

    fun getDescriptionTooltip(): List<Text> {
        val desc = replaceProperty(replaceProperty(translate("desc").string, '$')
        { getPlaceholder(it) }, '@') {
            val name = if (it.startsWith(".")) "wynnlib.ability.name${it.lowercase()}" else it
            from(name).translate().string
        }
        return formattingLines(desc, Formatting.GRAY.toString()).toList()
    }

    fun getTooltip(build: AbilityBuild? = null): List<Text> {
        val tree = AbilityRegistry.fromCharacter(getCharacter())
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(translate().formatted(tier.getFormatting()).formatted(Formatting.BOLD))
        if (getTier().getLevel() == 0){
            val spell = BoundSpellProperty.from(this)
            if (spell != null){
                tooltip.add(spell.getSpell().getComboText(getCharacter()))
            }else if(isMainAttack()){
                tooltip.add(Translations.TOOLTIP_ABILITY_CLICK_COMBO.translate().formatted(Formatting.GOLD)
                    .append(": ").append(character.getMainAttackKey().translate()
                        .formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD)))
            }
        }
        tooltip.add(LiteralText.EMPTY)
        tooltip.addAll(getDescriptionTooltip())
        //Add effect tooltip
        val propertyTooltip = getPropertiesTooltip()
        if (propertyTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(propertyTooltip)
        }
        //Add blocking abilities
        val incompatibles = getBlockAbilities()
        if (incompatibles.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_ABILITY_BLOCKS.translate().formatted(Formatting.RED))
            incompatibles.forEach {
                val color = if (build == null || !build.hasAbility(it)){
                    Formatting.GRAY
                }else{
                    Formatting.DARK_RED
                }
                tooltip.add(LiteralText("- ").formatted(Formatting.RED)
                    .append(it.translate().formatted(color)))
            }
        }
        if (isMainAttack())
            return tooltip
        tooltip.add(LiteralText.EMPTY)
        val apReq = if (build == null || build.hasAbility(this)){
            LiteralText("")
        }else if(build.getSpareAbilityPoints() >= getAbilityPointCost()){
            Symbol.TICK.asText().append(" ")
        }else{
            Symbol.CROSS.asText().append(" ")
        }
        //Requirements
        tooltip.add(apReq.append(TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY))
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(LiteralText(getAbilityPointCost().toString()).formatted(Formatting.WHITE)))
        if (dependency != null){
            getAbilityDependency()?.let {
                val dependencyReq = if (build == null || build.hasAbility(this)){
                    LiteralText("")
                }else if(build.hasAbility(it)){
                    Symbol.TICK.asText().append(" ")
                }else{
                    Symbol.CROSS.asText().append(" ")
                }
                tooltip.add(dependencyReq.append(TOOLTIP_ABILITY_DEPENDENCY.formatted(Formatting.GRAY))
                    .append(LiteralText(": ").formatted(Formatting.GRAY))
                    .append(it.formatted(Formatting.WHITE)))
            }
        }
        tree.getArchetypes().filter { getArchetypeRequirement(it) != 0 }.forEach {
            val requirement = getArchetypeRequirement(it)
            val archReq = if (build == null || build.hasAbility(this)){
                LiteralText("")
            }else if(build.getArchetypePoint(it) >= requirement){
                Symbol.TICK.asText().append(" ")
            }else{
                Symbol.CROSS.asText().append(" ")
            }
            val points = if (build == null || build.hasAbility(this)){
                LiteralText(requirement.toString()).formatted(Formatting.WHITE)
            }else if(build.getArchetypePoint(it) >= requirement){
                LiteralText(build.getArchetypePoint(it).toString()).formatted(Formatting.WHITE)
                    .append(LiteralText("/${requirement}").formatted(Formatting.GRAY))
            }else{
                LiteralText(build.getArchetypePoint(it).toString()).formatted(Formatting.RED)
                    .append(LiteralText("/${requirement}").formatted(Formatting.GRAY))
            }
            val title = TOOLTIP_ABILITY_MIN_ARCHETYPE.translate(null, it.translate().string)
            tooltip.add(archReq.append(title.formatted(Formatting.GRAY))
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(points))
        }
        getArchetype()?.let {
            tooltip.add(LiteralText.EMPTY)
            val title = TOOLTIP_ARCHETYPE_TITLE.translate(null, it.translate().string)
            tooltip.add(title.formatted(it.getFormatting()).formatted(Formatting.BOLD))
        }
        return tooltip
    }

    fun updateEntries(container: EntryContainer): Boolean {
        var flag = true
        for (property in properties.values) {
            if(!property.updateEntries(container)){
                flag = false
            }
        }
        return flag
    }

    fun getAbilityTree(): AbilityTree = AbilityRegistry.fromCharacter(character)

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = id.lowercase()
        if (label == "desc"){
            return "wynnlib.ability.desc.$key"
        }
        return "wynnlib.ability.name.$key"
    }

    override fun hashCode(): Int {
        return getKey().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Ability) {
            return getKey() == other.getKey()
        }
        return false
    }

    override fun toString(): String {
        return getKey()
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
        companion object {
            fun ofCharacter(character: CharacterClass): Tier {
                return when (character) {
                    CharacterClass.WARRIOR -> WARRIOR_SPELL
                    CharacterClass.ARCHER -> ARCHER_SPELL
                    CharacterClass.MAGE -> MAGE_SPELL
                    CharacterClass.ASSASSIN -> ASSASSIN_SPELL
                    CharacterClass.SHAMAN -> SHAMAN_SPELL
                }
            }
        }

        fun getLevel(): Int = level

        fun getFormatting(): Formatting = formatting

        fun getLockedTexture(): ItemStack = locked

        fun getUnlockedTexture(): ItemStack = unlocked

        fun getActiveTexture(): ItemStack = active
    }
}