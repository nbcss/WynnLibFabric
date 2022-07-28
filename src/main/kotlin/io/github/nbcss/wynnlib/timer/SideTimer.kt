package io.github.nbcss.wynnlib.timer

import net.minecraft.text.Text

interface SideTimer: ITimer {
    fun getDisplayText(): Text
}