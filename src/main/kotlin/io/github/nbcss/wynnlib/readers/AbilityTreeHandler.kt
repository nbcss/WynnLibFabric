package io.github.nbcss.wynnlib.readers

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.InventoryUpdateEvent
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.clearFormatting
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import java.util.regex.Pattern

object AbilityTreeHandler: EventHandler<InventoryUpdateEvent> {
    private val titlePattern = Pattern.compile("(.+) Abilities")
    private val unlockPattern = Pattern.compile("Unlock (.+) ability")
    private var processor: Processor? = null
    private var lastPage: Int? = null
    private var lastStacks: List<ItemStack> = emptyList()
    private var activeAbilities: Set<Ability> = emptySet()
    private var mutableAbilities: Set<Ability> = emptySet()
    private var character: CharacterClass? = null
    override fun handle(event: InventoryUpdateEvent) {
        val matcher = titlePattern.matcher(event.title.asString())
        if (matcher.find()) {
            CharacterClass.fromId(matcher.group(1))?.let {
                setAbilities(it, event.stacks)
            }
        } else {
            character = null
            lastPage = null
            activeAbilities = emptySet()
            mutableAbilities = emptySet()
            lastStacks = emptyList()
        }
        processor?.next()
    }

    private fun setAbilities(character: CharacterClass, stacks: List<ItemStack>) {
        this.character = character
        this.lastStacks = stacks
        this.activeAbilities = emptySet()
        this.mutableAbilities = emptySet()
        this.lastPage = null
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
            val abilityList: MutableList<Pair<Ability, Int>> = mutableListOf()
            for (pair in values) {
                val ability = pair.second.firstOrNull { it.getPage() == page } ?: continue
                val tooltip = pair.first.getTooltip(player, TooltipContext.Default.NORMAL)
                for (s in tooltip.filter { it.asString() == "" && it.siblings.isNotEmpty() }
                    .map { it.siblings[0].asString() }) {
                    if (s == "You already unlocked this ability") {
                        abilityList.add(ability to 1)
                        break
                    }else if(s == "Right Click to undo") {
                        abilityList.add(ability to 2)
                        break
                    }
                }
            }
            lastPage = page
            activeAbilities = abilityList.filter { it.second == 1 }.map { it.first }.toSet()
            mutableAbilities = abilityList.filter { it.second == 2 }.map { it.first }.toSet()
        }
    }

    fun getCurrentProcessor(): Processor? = processor

    fun clearProcessor() {
        this.processor = null
    }

    fun getLastPage(): Int? = lastPage

    fun getLastStacks(): List<ItemStack> = lastStacks

    fun getCharacter(): CharacterClass? = character

    fun getActiveAbilities(): Set<Ability> = activeAbilities

    fun getMutableAbilities(): Set<Ability> = mutableAbilities

    fun setProcessor(processor: Processor) {
        if (this.processor == null){
            this.processor = processor
            processor.next()
        }
    }
}