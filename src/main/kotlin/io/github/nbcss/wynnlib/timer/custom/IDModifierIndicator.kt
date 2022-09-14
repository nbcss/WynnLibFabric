package io.github.nbcss.wynnlib.timer.custom

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.i18n.SuffixTranslation
import io.github.nbcss.wynnlib.timer.BasicTimer
import io.github.nbcss.wynnlib.timer.StatusEntry
import io.github.nbcss.wynnlib.utils.colorOf
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.regex.Pattern

class IDModifierIndicator(private val id: Identification,
                          private val value: Int,
                          entry: StatusEntry,
                          startTime: Long):
    BasicTimer(entry, startTime) {
    companion object {
        private val numberPattern = Pattern.compile("^([+\\-]\\d+)")
        private val idMap: Map<String, Identification> = mapOf(
            pairs = Identification.getAll().map { "$${it.suffix} ${it.displayName}" to it }.toTypedArray()
        )

        fun fromStatus(entry: StatusEntry, worldTime: Long): IDModifierIndicator? {
            val matcher = numberPattern.matcher(entry.name)
            if (matcher.find()) {
                val value = matcher.group(1)
                val name = entry.name.replaceFirst(value, "$")
                idMap[name]?.let {
                    return IDModifierIndicator(it, value.toInt(), entry, worldTime)
                }
            }
            return null
        }
    }

    override fun getDisplayText(): Text {
        val color = colorOf(if (id.inverted) -value else value)
        val bonus = SuffixTranslation.withSuffix(value, id.suffix).formatted(color)
        return LiteralText(entry.icon).append(" ").append(bonus).append(" ")
            .append(id.translate(Formatting.GRAY, null))
    }
}