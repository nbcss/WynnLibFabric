package io.github.nbcss.wynnlib.readers

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.utils.clickSlot
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.screen.slot.SlotActionType
import java.util.function.Consumer

class AbilityTreeReader(private val character: CharacterClass,
                        private val callback: Consumer<AbilityTreeReader>): Processor {
    companion object {
        fun readActiveNodes(character: CharacterClass, callback: Consumer<AbilityTreeReader>) {
            val processor = AbilityTreeReader(character, callback)
            AbilityTreeHandler.setProcessor(processor)
        }
    }
    private val fixedAbilities: MutableSet<Ability> = mutableSetOf()
    private val mutableAbilities: MutableSet<Ability> = mutableSetOf()
    private var maxPoints: Int? = null
    private var reset: Boolean = true
    private var dead: Boolean = false

    override fun next() {
        if (AbilityTreeHandler.getCurrentProcessor() != this)
            return
        val lastStacks = AbilityTreeHandler.getLastStacks()
        if (dead || AbilityTreeHandler.getCharacter() != character || lastStacks.size != 90) {
            AbilityTreeHandler.clearProcessor()
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
        fixedAbilities.addAll(AbilityTreeHandler.getActiveAbilities())
        mutableAbilities.addAll(AbilityTreeHandler.getMutableAbilities())
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
            AbilityTreeHandler.clearProcessor()
            callback.accept(this)
        }
    }

    fun getActiveAbilities(): Collection<Ability> {
        return fixedAbilities
    }

    fun getMutableAbilities(): Collection<Ability> {
        return mutableAbilities
    }

    fun getMaxPoints(): Int {
        return maxPoints ?: 0
    }
}