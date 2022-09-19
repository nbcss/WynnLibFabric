package io.github.nbcss.wynnlib.events

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext

class RenderWorldEvent(val context: WorldRenderContext) {
    companion object: EventHandler.HandlerList<RenderWorldEvent>()
}