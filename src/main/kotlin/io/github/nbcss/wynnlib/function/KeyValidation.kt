package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.PlayerSendChatEvent
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

object KeyValidation: EventHandler<PlayerSendChatEvent> {
    private const val prefix = "/wynnlib active"
    override fun handle(event: PlayerSendChatEvent) {
        if (event.message.startsWith(prefix)) {
            val key = event.message.substring(prefix.length).trim()
            if (key == ""){
                Settings.validateKeys()
                event.player.sendMessage(LiteralText("Reloaded ").formatted(Formatting.YELLOW)
                    .append(if (Settings.isTester())
                        LiteralText("OK").formatted(Formatting.GREEN)
                    else
                        LiteralText("Reject").formatted(Formatting.RED)), false)
            }else if (Settings.addKey(key)) {
                event.player.sendMessage(LiteralText("Success").formatted(Formatting.GREEN), false)
            }else{
                event.player.sendMessage(LiteralText("Invalid Key").formatted(Formatting.RED), false)
            }
            event.cancelled = true
        }
    }
}