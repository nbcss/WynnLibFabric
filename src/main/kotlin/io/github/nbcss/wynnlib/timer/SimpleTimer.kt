package io.github.nbcss.wynnlib.timer

import net.minecraft.text.Text

class SimpleTimer(private val icon: String,
                  private val name: String,
                  time: String): ITimer {

    override fun getDisplayText(): Text {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Double? {
        TODO("Not yet implemented")
    }

    override fun getFullDuration(): Double? {
        TODO("Not yet implemented")
    }

    override fun getKey(): String {
        TODO("Not yet implemented")
    }
}