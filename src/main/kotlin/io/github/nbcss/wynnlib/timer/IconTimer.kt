package io.github.nbcss.wynnlib.timer

import net.minecraft.util.Identifier

interface IconTimer: ITimer {
    fun getIcon(): Identifier
    fun isCooldown(): Boolean
}