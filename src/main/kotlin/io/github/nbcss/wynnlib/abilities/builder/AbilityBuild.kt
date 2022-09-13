package io.github.nbcss.wynnlib.abilities.builder

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_LIST
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.registry.AbilityBuildStorage
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*

class AbilityBuild(private val tree: AbilityTree,
                   private val maxPoints: Int = MAX_AP,
                   abilities: Set<Ability> = emptySet(),
                   private val id: String = UUID.randomUUID().toString()): BaseItem, Keyed {
    private var name: String = ""
    companion object {
        val ICON = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        const val MAX_AP = 45
        const val VER = '0'
        const val BUFFER = 15
        fun fromData(data: JsonObject): AbilityBuild? {
            return try{
                val id = data["id"].asString
                val character = CharacterClass.fromId(data["class"].asString)!!
                val tree = AbilityRegistry.fromCharacter(character)
                val name = data["name"].asString
                val abilities = data["abilities"].asJsonArray.mapNotNull {
                    AbilityRegistry.get(it.asString)
                }.filter { it.getCharacter() == character }
                val build = AbilityBuild(tree, MAX_AP, abilities.toSet(), id)
                build.setName(name)
                build
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun fromEncoding(encoding: String): AbilityBuild? {
            return try{
                val ver = encoding[3]
                if (ver == VER) {
                    val character = CharacterClass.fromPrefix(encoding.substring(0..1))!!
                    val tree = AbilityRegistry.fromCharacter(character)
                    val abilities = tree.getAbilities().filter { it.getIndex() >= 0 }.filter {
                        (fromBase64(encoding[4 + it.getIndex() / 6]) and (1 shl (5 - it.getIndex() % 6))) != 0
                    }.toSet()
                    return AbilityBuild(tree, abilities = abilities)
                }
                null
            }catch (e: Exception) {
                null
            }
        }
    }
    private val paths: MutableMap<Ability, List<Ability>> = mutableMapOf()
    private val activeNodes: MutableSet<Ability> = mutableSetOf()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private val orderList: MutableList<Ability> = mutableListOf()
    private var cost: Int = 0
    private var level: Int = 1
    private var archetype: Archetype? = null
    private var encoding: String = generateEncoding()
    init {
        activeNodes.clear()
        activeNodes.addAll(abilities.filter { !it.isMainAttack() })
        cost = 0
        for (ability in activeNodes) {
            cost += ability.getAbilityPointCost()
            ability.getArchetype()?.let {
                archetypePoints[it] = 1 + (archetypePoints[it] ?: 0)
            }
        }
        validate()
    }

    fun setName(name: String) {
        this.name = name
        if (AbilityBuildStorage.has(getKey())){
            AbilityBuildStorage.markDirty()
        }
    }

    fun getActivateOrders(): List<Ability> = orderList

    fun getTree(): AbilityTree = tree

    fun removeAbility(ability: Ability) {
        activeNodes.remove(ability)
        validate()
        if (AbilityBuildStorage.has(getKey())){
            AbilityBuildStorage.markDirty()
        }
    }

    fun addAbility(ability: Ability): Boolean {
        paths[ability]?.let {
            if (it.isEmpty())
                return false
            for (node in it) {
                activeNodes.add(node)
                cost += ability.getAbilityPointCost()
                ability.getArchetype()?.let { arch ->
                    archetypePoints[arch] = 1 + (archetypePoints[arch] ?: 0)
                }
            }
            validate()
            if (AbilityBuildStorage.has(getKey())){
                AbilityBuildStorage.markDirty()
            }
            return true
        }
        return false
    }

    fun isUnlockable(ability: Ability): Boolean {
        val path = paths[ability]
        return path != null && path.isNotEmpty()
    }

    fun clear() {
        activeNodes.clear()
        cost = 0
        validate()
        if (AbilityBuildStorage.has(getKey())){
            AbilityBuildStorage.markDirty()
        }
    }

    private fun validate() {
        //reset current state
        orderList.clear()
        archetypePoints.clear()
        cost = 0
        //put fixed abilities first
        //validation
        val validated: MutableSet<Ability> = HashSet()
        var queue: MutableList<Ability> = mutableListOf()
        tree.getRootAbility()?.let { queue.add(it) }
        var lastSkip: MutableSet<Ability> = HashSet()
        while (true) {
            queue.addAll(lastSkip)
            val skipped: MutableSet<Ability> = HashSet()
            val nextQueue: Queue<Ability> = LinkedList()
            for (ability in queue) {
                if (ability !in activeNodes || ability in validated){
                    continue    //if not active by user, it must stay inactive
                }
                //check whether eligible to activating the node
                if (canUnlock(ability, validated)){
                    validated.add(ability)
                    cost += ability.getAbilityPointCost()
                    ability.getArchetype()?.let {
                        archetypePoints[it] = 1 + (archetypePoints[it] ?: 0)
                    }
                    orderList.add(ability)
                    ability.getSuccessors().filter { it in activeNodes }.forEach { nextQueue.add(it) }
                }else{
                    skipped.add(ability)
                }
            }
            if (nextQueue.isNotEmpty()) {
                nextQueue.addAll(skipped)
                queue = nextQueue.sortedBy { it.getPage() }.toMutableList()
                continue
            }
            if(skipped == lastSkip)
                break
            lastSkip = skipped
        }
        //replace active nodes with all validated nodes
        activeNodes.clear()
        activeNodes.addAll(validated)
        //compute paths
        paths.clear()
        tree.getAbilities().forEach { paths[it] = mutableListOf() }
        //compute path
        for (ability in activeNodes) {
            for (successor in ability.getSuccessors()) {
                if (canUnlock(successor, activeNodes)){
                    paths[successor] = listOf(successor)
                }
            }
        }
        tree.getRootAbility()?.let {
            if (it !in activeNodes) paths[it] = listOf(it)
        }
        encoding = generateEncoding()
        level = if (cost > 0) WynnValues.getAPLevelReq(cost) else 1
        archetype = archetypePoints.filter { it.value > 0 }.maxByOrNull { it.value }?.key
    }

    private fun canUnlock(ability: Ability, nodes: Collection<Ability>): Boolean {
        if (getSpareAbilityPoints() < ability.getAbilityPointCost())
            return false
        if (tree.getArchetypes().any { ability.getArchetypeRequirement(it) > (archetypePoints[it] ?: 0) })
            return false
        if (ability.getBlockAbilities().any { it in nodes })
            return false
        val dependency = ability.getAbilityDependency()
        if (dependency != null && dependency !in nodes)
            return false
        return true
    }

    private fun generateEncoding(): String {
        var encoding = "${tree.character.getPrefix()}-$VER"
        val array = IntArray(BUFFER)
        for (ability in activeNodes) {
            val index = ability.getIndex()
            if (index >= 0 && index < BUFFER * 6) {
                val i = index / 6
                val modifier = 1 shl (5 - index % 6)
                array[i] = array[i] or modifier
            }
        }
        for (i in array) {
            encoding += toBase64(i)
        }
        return encoding
    }

    fun getActiveAbilities(): Set<Ability> = tree.getMainAttackAbility()?.let {
        activeNodes.union(setOf(it))
    } ?: activeNodes

    fun getTotalCost(): Int = cost

    fun getMainArchetype(): Archetype? = archetype

    fun getSpareAbilityPoints(): Int = maxPoints - cost

    fun getArchetypePoint(archetype: Archetype): Int = archetypePoints[archetype] ?: 0

    fun hasAbility(ability: Ability): Boolean = ability in activeNodes || ability.isMainAttack()

    fun getEncoding(): String = encoding

    fun getCustomName(): String = name

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
        tooltip.add(Translations.TOOLTIP_COMBAT_LV_REQ.formatted(Formatting.GRAY)
            .append(LiteralText(": $level").formatted(Formatting.GRAY)))
        tooltip.add(Translations.TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY)
            .append(LiteralText(": $cost").formatted(Formatting.GRAY)))
        //abilities
        if (activeNodes.size > 0) {
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_ABILITY_LIST.formatted(Formatting.GRAY).append(": ")
                .append(LiteralText("${activeNodes.size}").formatted(Formatting.WHITE)))
            activeNodes.sortedWith { x, y ->
                val tier = y.getTier().compareTo(x.getTier())
                return@sortedWith if (tier != 0) tier else
                    x.translate().string.compareTo(y.translate().string)
            }.take(5).forEach {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(it.formatted(it.getTier().getFormatting())))
            }
            if (activeNodes.size > 5) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(LiteralText("...").formatted(Formatting.DARK_GRAY)))
            }
        }
        return tooltip
    }

    override fun getKey(): String = id

    fun getData(): JsonObject {
        val data = JsonObject()
        data.addProperty("id", id)
        data.addProperty("name", name)
        data.addProperty("class", tree.character.getKey())
        val array = JsonArray()
        for (ability in activeNodes) {
            array.add(ability.getKey())
        }
        data.add("abilities", array)
        return data
    }
}