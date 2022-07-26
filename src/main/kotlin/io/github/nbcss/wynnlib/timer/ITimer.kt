package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

interface ITimer: Keyed {
    fun getDisplayText(): Text
    fun getDuration(): Double?
    fun getFullDuration(): Double?
}