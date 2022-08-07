package io.github.nbcss.wynnlib.readers

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.utils.clearFormatting
import io.github.nbcss.wynnlib.utils.clickSlot
import net.minecraft.screen.slot.SlotActionType
import java.util.regex.Pattern

class AbilityTreeUnlocker(private val character: CharacterClass,
                          private val abilities: List<Ability>): Processor {
    companion object {
        private val unlockPattern = Pattern.compile("Unlock (.+) ability")
        fun activeNodes(character: CharacterClass, abilities: List<Ability>) {
            val processor = AbilityTreeUnlocker(character, abilities)
            AbilityTreeHandler.setProcessor(processor)
        }
    }
    private var currentIndex: Int = 0
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
        if (currentIndex >= abilities.size || currentPage == null) {
            //task complete or page error
            dead = true
            AbilityTreeHandler.clearProcessor()
            return
        }
        val ability = abilities[currentIndex]
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
            val unlockMatcher = unlockPattern.matcher(name)
            if (unlockMatcher.find() && unlockMatcher.group(1) == ability.getName()){
                clickSlot(slotId, 0, SlotActionType.PICKUP)
            }else if(name == ability.getName()){
                currentIndex += 1
                next()
            }else{
                println("Incorrect ability name $name; data outdated?")
                dead = true
                AbilityTreeHandler.clearProcessor()
            }
        }
    }
}