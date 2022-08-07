package io.github.nbcss.wynnlib.timer.indicators

import io.github.nbcss.wynnlib.timer.*
import java.util.regex.Pattern
import kotlin.math.max

class TypedStatusTimer(private val type: StatusType,
                       values: List<Int>,
                       entry: StatusEntry,
                       startTime: Long):
    AbstractFooterEntryTimer(entry, startTime) {
    private val maxDuration: Double? = entry.duration?.plus(1)?.toDouble()
    private var currentValues: List<Int> = values.toMutableList()
    private var maxValues: List<Int> = values.toMutableList()

    fun getValues(): List<Int> = currentValues

    fun getMaxValues(): List<Int> = maxValues

    override fun updateEntry(currentEntry: StatusEntry) {
        super.updateEntry(currentEntry)
        if (currentValues.isEmpty())
            return //don't need to update if it doesn't have value
        val matcher = numberPattern.matcher(currentEntry.name)
        var index = 0
        val nextValues: MutableList<Int> = mutableListOf()
        val nextMaxValues: MutableList<Int> = mutableListOf()
        while (matcher.find()) {
            val value = matcher.group(1).toInt()
            nextValues.add(value)
            if (index < maxValues.size) {
                nextMaxValues.add(max(maxValues[index], value))
            }
            index += 1
        }
        currentValues = nextValues
        maxValues = nextMaxValues
    }

    override fun getKey(): String = type.getKey()

    override fun getFullDuration(): Double? = maxDuration

    override fun asSideIndicator(): SideIndicator? {
        return type.asSideIndicator(this)
    }

    override fun asIconIndicator(): IconIndicator? {
        return type.asIconIndicator(this)
    }

    companion object {
        private val numberPattern = Pattern.compile("(\\d+)")
        fun fromStatus(entry: StatusEntry, worldTime: Long): ITimer? {
            val matcher = numberPattern.matcher(entry.name)
            val values: MutableList<Int> = mutableListOf()
            var name = entry.name
            while (matcher.find()) {
                val value = matcher.group(1)
                values.add(value.toInt())
                name = name.replaceFirst(value, "$")
            }
            StatusType.fromDisplay(entry.icon, name)?.let {
                return it.createTimer(entry, values, worldTime)
            }
            return null
        }
    }
}