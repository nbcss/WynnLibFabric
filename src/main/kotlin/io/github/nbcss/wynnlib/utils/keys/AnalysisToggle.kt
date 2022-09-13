package io.github.nbcss.wynnlib.utils.keys

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.WynnLibKeybindings.getToggleAnalyzeKeybinding
import io.github.nbcss.wynnlib.mixins.keys.BoundKeyAccessor

object AnalysisToggle {
    private val callback: ToggleCallback = ToggleCallback({
        KeysKit.isKeyDown((getToggleAnalyzeKeybinding() as BoundKeyAccessor).getboundKey().code)
    }){
        if (it) {
            Settings.toggleAnalysisMode()
        }
    }

    fun tick() {
        callback.tick()
    }
}