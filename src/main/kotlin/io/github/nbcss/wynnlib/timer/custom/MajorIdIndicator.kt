package io.github.nbcss.wynnlib.timer.custom

import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.i18n.Translatable.Companion.from
import io.github.nbcss.wynnlib.timer.BasicTimer
import io.github.nbcss.wynnlib.timer.StatusEntry
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.regex.Pattern

class MajorIdIndicator(private val majorId: MajorId,
                       entry: StatusEntry,
                       startTime: Long):
    BasicTimer(entry, startTime) {
    companion object {
        private val numberPattern = Pattern.compile("^\\+(.+) Major ID$")
        private val INDICATOR_NAME = from("wynnlib.indicator.major_id")
        fun fromStatus(entry: StatusEntry, worldTime: Long): MajorIdIndicator? {
            val matcher = numberPattern.matcher(entry.name)
            if (matcher.find()) {
                val name = matcher.group(1)
                MajorId.fromDisplayName(name)?.let {
                    return MajorIdIndicator(it, entry, worldTime)
                }
            }
            return null
        }
    }

    override fun getDisplayText(): Text {
        val name = INDICATOR_NAME.formatted(Formatting.GRAY, null,
            majorId.translate().string)
        return LiteralText(entry.icon).append(" ").append(name)
    }
}