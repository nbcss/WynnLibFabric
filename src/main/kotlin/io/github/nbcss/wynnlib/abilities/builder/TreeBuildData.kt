package io.github.nbcss.wynnlib.abilities.builder

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.registry.AbilityBuildStorage
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*

/**
 * Contain the abilities' data of a tree build, but will not
 * validate whether the given nodes are valid input.
 */
class TreeBuildData(private val tree: AbilityTree,
                    private val id: String = UUID.randomUUID().toString()): BaseItem, Keyed {
    companion object {
        val ICON = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        const val VER = '0'
        const val BUFFER = 15
        fun fromData(data: JsonObject): TreeBuildData? {
            return try{
                val id = data["id"].asString
                val character = CharacterClass.fromId(data["class"].asString)!!
                val tree = AbilityRegistry.fromCharacter(character)
                val name = data["name"].asString
                val abilities = data["abilities"].asJsonArray.mapNotNull {
                    AbilityRegistry.get(it.asString)
                }.filter { it.getCharacter() == character }
                val build = TreeBuildData(tree, id)
                build.setAbilities(abilities.toSet())
                TreeBuildContainer(build)
                build.setName(name)
                build
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun fromEncoding(encoding: String): TreeBuildData? {
            return try{
                val ver = encoding[3]
                if (ver == VER) {
                    val character = CharacterClass.fromPrefix(encoding.substring(0..1))!!
                    val tree = AbilityRegistry.fromCharacter(character)
                    val abilities = tree.getAbilities().filter { it.getIndex() >= 0 }.filter {
                        (fromBase64(encoding[4 + it.getIndex() / 6]) and (1 shl (5 - it.getIndex() % 6))) != 0
                    }.toSet()
                    val build = TreeBuildData(tree)
                    build.setAbilities(abilities.toSet())
                    TreeBuildContainer(build)
                    return build
                }
                null
            }catch (e: Exception) {
                null
            }
        }
    }

    private var name: String = ""
    private val abilities: MutableSet<Ability> = mutableSetOf()
    private val archetypes: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private var cost: Int = 0
    private var level: Int = 1
    private var archetype: Archetype? = null
    private var encoding: String = generateEncoding()

    fun copy(): TreeBuildData {
        val copy = TreeBuildData(tree)
        copy.setAbilities(abilities)
        return copy
    }

    fun setName(name: String) {
        this.name = name
        if (AbilityBuildStorage.has(getKey())){
            AbilityBuildStorage.markDirty()
        }
    }

    fun clear() {
        cost = 0
        abilities.clear()
        archetypes.clear()
        updateData()
    }

    fun setAbilities(abilities: Set<Ability>) {
        this.abilities.clear()
        this.archetypes.clear()
        cost = 0
        for (ability in abilities.filter { !it.isMainAttack() }) {
            cost += ability.getAbilityPointCost()
            ability.getArchetype()?.let { it ->
                archetypes[it] = 1 + (archetypes[it] ?: 0)
            }
            this.abilities.add(ability)
        }
        updateData()
    }

    fun removeAbility(ability: Ability): Boolean {
        if (abilities.remove(ability)) {
            cost -= ability.getAbilityPointCost()
            ability.getArchetype()?.let { arch ->
                archetypes[arch] = -1 + (archetypes[arch] ?: 0)
            }
            updateData()
            return true
        }
        return false
    }

    fun addAbility(ability: Ability): Boolean {
        if (abilities.add(ability)) {
            cost += ability.getAbilityPointCost()
            ability.getArchetype()?.let { arch ->
                archetypes[arch] = 1 + (archetypes[arch] ?: 0)
            }
            updateData()
            return true
        }
        return false
    }

    fun getActiveAbilities(): Set<Ability> = tree.getMainAttackAbility()?.let {
        abilities.union(setOf(it))
    } ?: abilities

    fun getTotalCost(): Int = cost

    fun getMainArchetype(): Archetype? = archetype

    fun getArchetypePoint(archetype: Archetype): Int = archetypes[archetype] ?: 0

    fun hasAbility(ability: Ability): Boolean = ability in abilities || ability.isMainAttack()

    fun getEncoding(): String = encoding

    fun getCustomName(): String = name

    fun getTree(): AbilityTree = tree

    fun getData(): JsonObject {
        val data = JsonObject()
        data.addProperty("id", id)
        data.addProperty("name", name)
        data.addProperty("class", tree.character.getKey())
        val array = JsonArray()
        for (ability in abilities) {
            array.add(ability.getKey())
        }
        data.add("abilities", array)
        return data
    }

    private fun generateEncoding(): String {
        var encoding = "${tree.character.getPrefix()}-${VER}"
        val array = IntArray(BUFFER)
        for (ability in abilities) {
            val index = ability.getIndex()
            if (index >= 0 && index < BUFFER * 6) {
                val i = index / 6
                val modifier = 1 shl (5 - index % 6)
                array[i] = array[i] or modifier
            }
        }
        for (i in array) encoding += toBase64(i)
        return encoding
    }

    private fun updateData() {
        encoding = generateEncoding()
        level = if (cost > 0) WynnValues.getAPLevelReq(cost) else 1
        archetype = archetypes.filter { it.value > 0 }.maxByOrNull { it.value }?.key
        if (AbilityBuildStorage.has(getKey())){
            AbilityBuildStorage.markDirty()
        }
    }

    override fun getDisplayText(): Text = LiteralText(getDisplayName()).formatted(Formatting.DARK_AQUA)

    override fun getDisplayName(): String = if (name != "") name else encoding

    override fun getIcon(): ItemStack {
        getMainArchetype()?.let {
            return it.getTexture()
        }
        return ICON
    }

    override fun getIconText(): String? {
        getMainArchetype()?.let {
            return it.getIconText()
        }
        return super.getIconText()
    }

    override fun getRarityColor(): Color {
        getMainArchetype()?.let {
            return Color.fromFormatting(it.getFormatting())
        }
        return Color.DARK_GRAY
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(getDisplayText())
        val archetypes = LiteralText("").formatted(Formatting.DARK_GRAY)
        tree.getArchetypes().forEachIndexed { i, archetype ->
            val point = getArchetypePoint(archetype)
            if (i > 0) archetypes.append("/")
            archetypes.append(LiteralText("$point").formatted(archetype.getFormatting()))
        }
        tooltip.add(archetypes)
        tooltip.add(LiteralText.EMPTY)
        //tooltip.add(tree.character.formatted(Formatting.GRAY))
        val classReq = tree.character.translate().formatted(Formatting.GRAY)
        val prefix = Translations.TOOLTIP_CLASS_REQ.formatted(Formatting.GRAY)
        tooltip.add(prefix.append(LiteralText(": ").formatted(Formatting.GRAY)).append(classReq))
        tooltip.add(
            Translations.TOOLTIP_COMBAT_LV_REQ.formatted(Formatting.GRAY)
            .append(LiteralText(": $level").formatted(Formatting.GRAY)))
        tooltip.add(
            Translations.TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY)
            .append(LiteralText(": $cost").formatted(Formatting.GRAY)))
        //abilities
        if (abilities.size > 0) {
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(
                Translations.TOOLTIP_ABILITY_LIST.formatted(Formatting.GRAY).append(": ")
                .append(LiteralText("${abilities.size}").formatted(Formatting.WHITE)))
            abilities.sortedWith { x, y ->
                val tier = y.getTier().compareTo(x.getTier())
                return@sortedWith if (tier != 0) tier else
                    x.translate().string.compareTo(y.translate().string)
            }.take(5).forEach {
                tooltip.add(
                    LiteralText("- ").formatted(Formatting.GRAY)
                    .append(it.formatted(it.getTier().getFormatting())))
            }
            if (abilities.size > 5) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(LiteralText("...").formatted(Formatting.DARK_GRAY)))
            }
        }
        return tooltip
    }

    override fun getKey(): String = id
}