package io.github.nbcss.wynnlib.abilities.builder

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*

class AbilityBuild(private val tree: AbilityTree,
                   private val maxPoints: Int = MAX_AP,
                   private val id: String = UUID.randomUUID().toString()): BaseItem, Keyed {
    private var icon: ItemStack = ICON.copy()
    private var name: String = ""
    companion object {
        val ICON = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        const val MAX_AP = 45
        const val VER = "0"
        const val BUFFER = 15
        fun fromData(data: JsonObject): AbilityBuild? {
            try{
                val id = data["id"].asString
                val character = CharacterClass.fromId(data["class"].asString)!!
                val tree = AbilityRegistry.fromCharacter(character)
                val name = data["name"].asString
                val abilities = data["abilities"].asJsonArray.mapNotNull {
                    AbilityRegistry.get(it.asString)
                }.filter { it.getCharacter() == character }
                val build = AbilityBuild(tree, MAX_AP, id)
                build.setName(name)
                build.setAbilities(abilities.toSet())
                return build
            }catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
    private val paths: MutableMap<Ability, List<Ability>> = mutableMapOf()
    private val activeNodes: MutableSet<Ability> = mutableSetOf()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private val orderList: MutableList<Ability> = mutableListOf()
    private var cost: Int = 0
    private var level: Int = 1
    private var encoding: String = generateEncoding()

    fun setName(name: String) {
        this.name = name
    }

    fun getActivateOrders(): List<Ability> = orderList

    fun removeAbility(ability: Ability) {
        activeNodes.remove(ability)
        validate()
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
            return true
        }
        return false
    }

    fun isUnlockable(ability: Ability): Boolean {
        val path = paths[ability]
        return path != null && path.isNotEmpty()
    }

    fun setAbilities(abilities: Set<Ability>) {
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

    fun getSpareAbilityPoints(): Int = maxPoints - cost

    fun getArchetypePoint(archetype: Archetype): Int = archetypePoints[archetype] ?: 0

    fun hasAbility(ability: Ability): Boolean = ability in activeNodes || ability.isMainAttack()

    fun getEncoding(): String = encoding

    override fun getDisplayText(): Text = LiteralText(getDisplayName()).formatted(Formatting.AQUA)

    override fun getDisplayName(): String = if (name != "") name else encoding

    override fun getIcon(): ItemStack = icon

    override fun getRarityColor(): Color {
        return Color.WHITE
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(getDisplayText())
        tooltip.add(LiteralText.EMPTY)
        //tooltip.add(tree.character.formatted(Formatting.GRAY))
        val classReq = tree.character.translate().formatted(Formatting.GRAY)
        val prefix = Translations.TOOLTIP_CLASS_REQ.formatted(Formatting.GRAY)
        tooltip.add(prefix.append(LiteralText(": ").formatted(Formatting.GRAY)).append(classReq))
        tooltip.add(Translations.TOOLTIP_COMBAT_LV_REQ.formatted(Formatting.GRAY)
            .append(LiteralText(": $level").formatted(Formatting.GRAY)))
        tooltip.add(Translations.TOOLTIP_ABILITY_POINTS.formatted(Formatting.GRAY)
            .append(LiteralText(": $cost").formatted(Formatting.GRAY)))
        tooltip.add(LiteralText.EMPTY)
        //abilities
        if (activeNodes.size > 0) {
            tooltip.add(LiteralText("Abilities: ").formatted(Formatting.GRAY)
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
            tooltip.add(LiteralText.EMPTY)
        }
        for (archetype in tree.getArchetypes()) {
            val point = getArchetypePoint(archetype)
            tooltip.add(archetype.formatted(Formatting.GRAY).append(": ")
                .append(LiteralText("$point")
                    .formatted(if (point > 0) Formatting.WHITE else Formatting.DARK_GRAY)))
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