package io.github.nbcss.wynnlib.utils.keys

import io.github.nbcss.wynnlib.Settings

object AnalysisToggle {
    private val callback: ToggleCallback = ToggleCallback({ KeysKit.isCtrlDown() }){
        if (it) {
            Settings.toggleAnalysisMode()
        }
    }

    fun tick() {
        callback.tick()
    }
}