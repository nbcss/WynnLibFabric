package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.abilities.IconTexture
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.data.PowderSpecial
import io.github.nbcss.wynnlib.events.ClientTickEvent
import io.github.nbcss.wynnlib.events.EventRegistry
import io.github.nbcss.wynnlib.items.identity.ItemProtectManager
import io.github.nbcss.wynnlib.registry.*
import io.github.nbcss.wynnlib.timer.status.StatusType
import io.github.nbcss.wynnlib.utils.Scheduler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents


@Suppress("UNUSED")
object WynnLibEntry: ModInitializer {
    private const val MOD_ID = "wynnlib"

    override fun onInitialize() {
        //Reload Settings & auto saving
        Settings.reload()
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            Scheduler.tick()
            ClientTickEvent.handleEvent(ClientTickEvent())
        })
        //Reload icons
        IconTexture.reload()
        //Load data
        AbilityRegistry.load()
        Identification.load() //id have to load after ability, because spell id need ability name from abilities...
        StatusType.load()
        MajorId.load()
        PowderSpecial.load()
        PowderRegistry.load()
        RegularEquipmentRegistry.load()
        IngredientRegistry.load()
        MaterialRegistry.load()
        RecipeRegistry.load()
        TomeRegistry.load()
        CharmRegistry.load()
        //Load local user info
        AbilityBuildStorage.load()
        ItemProtectManager.load()
        //Register keybindings
        WynnLibKeybindings.init()
        //Register events
        EventRegistry.registerEvents()
    }
}