package io.github.nbcss.wynnlib.readers

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.utils.clearFormatting
import io.github.nbcss.wynnlib.utils.clickSlot
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.screen.slot.SlotActionType
import java.util.regex.Pattern

class AbilityTreeModifier(private val character: CharacterClass,
                          private val abilities: List<Ability>,
                          private val removed: List<Ability>): Processor {
    companion object {
        private val unlockPattern = Pattern.compile("Unlock (.+) ability")
        fun modifyNodes(character: CharacterClass, abilities: List<Ability>, remove: List<Ability>) {
            val processor = AbilityTreeModifier(character, abilities, remove)
            AbilityTreeHandler.setProcessor(processor)
        }
    }
    private var removeIndex: Int = 0
    private var unlockIndex: Int = 0
    private var dead: Boolean = false

    override fun next() {
        if (AbilityTreeHandler.getCurrentProcessor() != this)
            return
        val lastStacks = AbilityTreeHandler.getLastStacks()
        if (dead || AbilityTreeHandler.getCharacter() != character || lastStacks.size != 90) {
            AbilityTreeHandler.clearProcessor()
            return
        }
        val currentPage = AbilityTreeHandler.getLastPage()
        if ((removeIndex >= removed.size && unlockIndex >= abilities.size) || currentPage == null) {
            //task complete or page error
            dead = true
            AbilityTreeHandler.clearProcessor()
            return
        }
        val ability = if (removeIndex < removed.size) removed[removeIndex] else abilities[unlockIndex]
        val targetPage = ability.getPage()
        if (targetPage < currentPage) {
            //go last page
            clickSlot(57, 0, SlotActionType.PICKUP)
        }else if (targetPage > currentPage) {
            //go next page
            clickSlot(59, 0, SlotActionType.PICKUP)
        }else{
            //click slot ! check it is unlocked
            val slotId = ability.getSlot()
            val stack = lastStacks[slotId]
            val name = clearFormatting(stack.name.asString())
            if (removeIndex < removed.size) {
                if (name == ability.getName() &&
                    stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL)
                        .filter { it.asString() == "" && it.siblings.isNotEmpty()}
                        .map { it.siblings[0].asString() }
                        .any { it == "Right Click to undo" }) {
                    clickSlot(slotId, 1, SlotActionType.QUICK_MOVE)
                }else{
                    val unlockMatcher = unlockPattern.matcher(name)
                    if (name == ability.getName() ||
                        (unlockMatcher.find() && unlockMatcher.group(1) == ability.getName())){
                        removeIndex += 1
                        next()
                    }else{
                        dead = true
                        AbilityTreeHandler.clearProcessor()
                    }
                }
            }else{
                val unlockMatcher = unlockPattern.matcher(name)
                if (unlockMatcher.find() && unlockMatcher.group(1) == ability.getName()){
                    clickSlot(slotId, 0, SlotActionType.PICKUP)
                }else if(name == ability.getName()){
                    unlockIndex += 1
                    next()
                }else{
                    println("Incorrect ability name $name; data outdated?")
                    dead = true
                    AbilityTreeHandler.clearProcessor()
                }
            }
        }
    }
}