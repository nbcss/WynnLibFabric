package io.github.nbcss.wynnlib.timer

import net.minecraft.text.Text
import java.util.regex.Pattern

object TimerManager {
    private val timerPattern = Pattern.compile("(§[0-9a-f].) §[0-9a-f](.+?) §[0-9a-f]\\((\\d\\d:\\d\\d)\\)")
    private val timerMap: MutableMap<String, ITimer> = mutableMapOf()
    private var worldTime: Long = 0

    fun updateWorldTime(time: Long) {
        synchronized(timerMap) {
            worldTime = time
            for (timer in timerMap.values) {
                timer.updateWorldTime(worldTime)
            }
        }
    }

    fun updateFooter(footer: Text) {
        if (footer.asString().contains("Status Effects")){
            for (sibling in footer.siblings) {
                val matcher = timerPattern.matcher(sibling.asString())
                while (matcher.find()) {
                    //println("icon: " + matcher.group(1))
                    //println("name: " + matcher.group(2))
                    //println("time: " + matcher.group(3))
                }
            }
        }
    }

    fun getSideTimers(): List<SideTimer> {
        return emptyList() //todo
    }
}