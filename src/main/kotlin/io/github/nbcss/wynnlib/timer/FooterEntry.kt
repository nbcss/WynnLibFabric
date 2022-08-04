package io.github.nbcss.wynnlib.timer

import net.minecraft.text.Text
import java.util.regex.Pattern

data class FooterEntry(val icon: String,
                  val name: String,
                  val duration: Int?) {
    companion object {
        //��f��b?��7 Windy Feet ��8(01:26)
        private val timerPattern = Pattern.compile(
            "(§[0-9a-f].) ?§[0-9a-f] ?(.+?) §[0-9a-f]\\((\\d\\d:\\d\\d|\\*\\*:\\*\\*)\\)")
        private val entries: MutableMap<String, FooterEntry> = mutableMapOf()

        fun updateFooter(footer: Text?) {
            if (footer != null && footer.asString().contains("Status Effects")){
                val list: MutableList<FooterEntry> = mutableListOf()
                for (sibling in footer.siblings) {
                    val matcher = timerPattern.matcher(sibling.asString())
                    while (matcher.find()) {
                        val icon = matcher.group(1)
                        val name = matcher.group(2)
                        val time = matcher.group(3).split(":")
                        val duration = if (time[0] == "**"){
                            null
                        }else{
                            time[0].toInt() * 60 + time[1].toInt()
                        }
                        list.add(FooterEntry(icon, name, duration))
                    }
                }
                synchronized(this) {
                    entries.clear()
                    for (entry in list) {
                        entries[entry.name] = entry
                        val timer = ITimer.fromEntry(entry)
                        TimerManager.registerTimer(timer)
                    }
                }
            }else{
                synchronized(this) {
                    entries.clear()
                }
            }
        }

        fun getFooterEntry(name: String): FooterEntry? {
            return entries[name]
        }
    }
}