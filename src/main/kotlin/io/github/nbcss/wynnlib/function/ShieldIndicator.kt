package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.events.ArmourStandUpdateEvent
import io.github.nbcss.wynnlib.events.ClientTickEvent
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.SpellCastEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items

object ShieldIndicator {
    private val client = MinecraftClient.getInstance()
    private val shields: MutableMap<Int, Group> = mutableMapOf()
    private val groups: MutableSet<Group> = mutableSetOf()
    private var currentGroup: Group? = null
    private var lastTrigger: Long = -1

    private fun newGroup(): Group {
        val group = Group(System.currentTimeMillis(), 0)
        groups.add(group)
        return group
    }

    object SpellTrigger: EventHandler<SpellCastEvent> {
        override fun handle(event: SpellCastEvent) {
            if (event.ability.getName() == "War Scream") {
                lastTrigger = 1
            }
        }
    }

    object EntitySpawn: EventHandler<ArmourStandUpdateEvent> {
        override fun handle(event: ArmourStandUpdateEvent) {
            val item = event.entity.armorItems.last()

            if (item.item == Items.DIAMOND_AXE && item.damage == 62) {
                val id = event.entity.id
                if (id in shields)
                    return
                val group = if ((id - 1) in shields) {
                    shields[id - 1]!!
                }else if ((id + 1) in shields) {
                    shields[id + 1]!!
                }else{
                    newGroup()
                }
                group.size += 1
                shields[id] = group
            }
        }
    }

    object Ticker: EventHandler<ClientTickEvent> {
        override fun handle(event: ClientTickEvent) {
            if (lastTrigger >= 0) {
                for (group in groups) {
                    if (group.timestamp - System.currentTimeMillis() <= 200) {
                        currentGroup = group
                        lastTrigger = -1
                        break
                    }
                }
            }
            for (pair in shields.toList()) {
                val entity = client.world?.getEntityById(pair.first)
                if (entity == null) {
                    shields.remove(pair.first)
                    pair.second.size -= 1
                }
            }
            for (group in groups.toList()) {
                if (group.size == 0) {
                    groups.remove(group)
                }
            }
            if (currentGroup != null && currentGroup !in groups) {
                currentGroup = null
            }
            currentGroup?.let {
                println(shields)
                println(it.size)
            }
        }
    }

    class Group(val timestamp: Long, var size: Int)
}