package io.github.nbcss.wynnlib.readers

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.InventoryUpdateEvent
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.clearFormatting
import io.github.nbcss.wynnlib.utils.clickSlot
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.SlotActionType
import java.util.function.Consumer
import java.util.regex.Pattern

class AbilityTreeReader(private val character: CharacterClass,
                        private val callback: Consumer<AbilityTreeReader>) {
    companion object: EventHandler<InventoryUpdateEvent> {
        private val titlePattern = Pattern.compile("(.+) Abilities")
        private val unlockPattern = Pattern.compile("Unlock (.+) ability")
        private var lastStacks: List<ItemStack> = emptyList()
        private var reader: AbilityTreeReader? = null
        private var activeAbilities: Set<Ability> = emptySet()
        private var character: CharacterClass? = null
        override fun handle(event: InventoryUpdateEvent) {
            val matcher = titlePattern.matcher(event.title.asString())
            if (matcher.find()) {
                CharacterClass.fromId(matcher.group(1))?.let {
                    setAbilities(it, event.stacks)
                }
            } else {
                character = null
                activeAbilities = emptySet()
                lastStacks = emptyList()
            }
            reader?.next()
        }

        private fun setAbilities(character: CharacterClass, stacks: List<ItemStack>) {
            this.character = character
            this.lastStacks = stacks
            this.activeAbilities = emptySet()
            val values: MutableList<Pair<ItemStack, List<Ability>>> = mutableListOf()
            stacks.take(54).forEachIndexed { index, stack ->
                if (!stack.isEmpty && stack.name.asString() != " "){
                    var name = clearFormatting(stack.name.asString())
                    val unlockMatcher = unlockPattern.matcher(name)
                    if (unlockMatcher.find()) {
                        name = unlockMatcher.group(1)
                    }
                    val abilities = AbilityRegistry.fromDisplayName(name)
                        .filter { it.getCharacter() == character }
                        .filter { it.getSlot() == index }
                    if (abilities.isNotEmpty()) {
                        values.add(stack to abilities)
                    }
                }
            }
            val page = values.map { it.second }.filter { it.size == 1 }.map { it[0].getPage() }.firstOrNull()
            if (page != null) {
                val player = MinecraftClient.getInstance().player
                val abilitySet: MutableSet<Ability> = mutableSetOf()
                for (pair in values) {
                    val ability = pair.second.firstOrNull { it.getPage() == page } ?: continue
                    val tooltip = pair.first.getTooltip(player, TooltipContext.Default.NORMAL)
                    if (tooltip.filter { it.asString() == "" && it.siblings.isNotEmpty() }
                            .map { it.siblings[0].asString() }
                            .any { it == "You already unlocked this ability" }) {
                        abilitySet.add(ability)
                    }
                }
                activeAbilities = abilitySet
            }
        }

        fun getReader(): AbilityTreeReader? = reader

        fun read(character: CharacterClass, callback: Consumer<AbilityTreeReader>) {
            reader = AbilityTreeReader(character, callback)
            reader!!.next()
        }
    }
    private val abilities: MutableSet<Ability> = mutableSetOf()
    private var maxPoints: Int? = null
    private var reset: Boolean = true
    private var dead: Boolean = false

    private fun next() {
        if (dead || Companion.character != character || lastStacks.size != 90) {
            reader = null
            return
        }
        if (reset) {
            val slotId = 57
            val lastPage = lastStacks[slotId]
            if (!lastPage.isEmpty) {
                clickSlot(slotId, 0, SlotActionType.PICKUP)
                return
            }
            reset = false
        }
        updatePage()
        val slotId = 59
        val nextPage = lastStacks[slotId]
        if (!nextPage.isEmpty) {
            clickSlot(slotId, 0, SlotActionType.PICKUP)
        }else{
            val pointStack = lastStacks[58]
            val tooltip = pointStack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
            maxPoints = tooltip.asSequence()
                .filter { it.siblings.isNotEmpty() }
                .map { it.siblings[0] }
                .filter { it.siblings.isNotEmpty() }
                .filter { it.siblings[0].asString() == "✦ Available Points: " }
                .map { it.siblings.last().asString() }
                .filter { it.isNotEmpty() }.toList()
                .firstNotNullOfOrNull { it.substring(1).toIntOrNull() }
            dead = true
            reader = null
            callback.accept(this)
        }
    }

    private fun updatePage() {
        if (character == Companion.character) {
            abilities.addAll(activeAbilities)
        }
    }

    fun getActiveAbilities(): Collection<Ability> {
        return abilities
    }

    fun getMaxPoints(): Int {
        return maxPoints ?: 0
    }
}