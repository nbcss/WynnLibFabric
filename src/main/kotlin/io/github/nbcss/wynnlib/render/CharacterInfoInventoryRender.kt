package io.github.nbcss.wynnlib.render

import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.InventoryRenderEvent
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

object CharacterInfoInventoryRender: EventHandler<InventoryRenderEvent> {
    private val client: MinecraftClient = MinecraftClient.getInstance()
    override fun handle(event: InventoryRenderEvent) {
        if (event.phase != InventoryRenderEvent.Phase.POST)
            return
        val title = event.screen.title.asString()
        if (title != "Character Info")
            return
        val handler = event.screen.screenHandler
        if (handler.slots.size < 63)
            return
        val abilityTreeItem = handler.getSlot(9).stack
        val ap = if (abilityTreeItem.isEmpty) 0 else
            abilityTreeItem.getTooltip(client.player, TooltipContext.Default.NORMAL)
                .asSequence()
                .filter { it.siblings.isNotEmpty() }
                .map { it.siblings[0] }
                .filter { it.siblings.size >= 2 }
                .filter { it.siblings[0].asString().contains("Unused Points: ") }
                .filter { it.siblings[1].asString().matches("\\d+".toRegex()) }
                .map { it.siblings[1].asString().toInt() }
                .firstOrNull() ?: 0
        val skillPointItem = handler.getSlot(4).stack
        val sp = if (skillPointItem.isEmpty) 0 else
            skillPointItem.getTooltip(client.player, TooltipContext.Default.NORMAL)
                .asSequence()
                .filter { it.siblings.isNotEmpty() }
                .map { it.siblings[0] }
                .filter { it.siblings.size >= 2 }
                .filter { it.siblings[0].asString() == "You have " }
                .filter { it.siblings[1].asString().matches("\\d+".toRegex()) }
                .map { it.siblings[1].asString().toInt() }
                .firstOrNull() ?: 0
        val posX = event.screenX.toFloat()
        val posY = event.screenY.toFloat() + 2
        val text = LiteralText("✦ $ap").formatted(Formatting.DARK_AQUA)
            .append(LiteralText("   ✦ $sp").formatted(Formatting.GREEN))
        event.matrices.push()
        event.matrices.translate(0.0, 0.0, 200.0)
        //client.textRenderer.draw(event.matrices, text, posX, posY, 0xFFFFFF)
        //client.textRenderer.drawWithShadow(event.matrices, text, posX, posY, Color.DARK_AQUA.code())
        RenderKit.renderOutlineText(event.matrices, text, posX, posY, Color.WHITE)
        event.matrices.pop()
    }
}