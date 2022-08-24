package io.github.nbcss.wynnlib.timer.indicators

import io.github.nbcss.wynnlib.timer.*
import java.util.regex.Pattern
import kotlin.math.max

class TypedStatusTimer(private val type: StatusType,
                       values: List<Double>,
                       entry: StatusEntry,
                       startTime: Long):
    AbstractFooterEntryTimer(entry, startTime) {
    private val maxDuration: Double? = entry.duration?.plus(1)?.toDouble()
    private var currentValues: List<Double> = values.toMutableList()
    private var lastValues: List<Double> = values.toMutableList()
    private var maxValues: List<Double> = values.toMutableList()
    private var lastUpdated: Long = startTime

    fun getValues(): List<Double> = currentValues

    fun getMaxValues(): List<Double> = maxValues

    override fun updateEntry(currentEntry: StatusEntry) {
        super.updateEntry(currentEntry)
        if (currentValues.isEmpty())
            return //don't need to update if it doesn't have value
        val matcher = numberPattern.matcher(currentEntry.name)
        var index = 0
        val nextValues: MutableList<Double> = mutableListOf()
        val nextMaxValues: MutableList<Double> = mutableListOf()
        while (matcher.find()) {
            val value = matcher.group(1).toDouble()
            nextValues.add(value)
            if (index < maxValues.size) {
                nextMaxValues.add(max(maxValues[index], value))
            }
            index += 1
        }
        if (currentValues != nextValues) {
            lastValues = currentValues
            currentValues = nextValues
            lastUpdated = getSyncTime()
        }
        maxValues = nextMaxValues
    }

    fun getLastUpdateTime(): Long = lastUpdated

    fun getLastValues(): List<Double> = lastValues

    override fun getKey(): String = type.getKey()

    override fun getFullDuration(): Double? = maxDuration

    override fun asSideIndicator(): SideIndicator? {
        return type.asSideIndicator(this)
    }

    override fun asIconIndicator(): IconIndicator? {
        return type.asIconIndicator(this)
    }

    companion object {
        private val numberPattern = Pattern.compile("(\\d+(\\.\\d+)?)")
        fun fromStatus(entry: StatusEntry, worldTime: Long): ITimer? {
            val matcher = numberPattern.matcher(entry.name)
            val values: MutableList<Double> = mutableListOf()
            var name = entry.name
            while (matcher.find()) {
                val value = matcher.group(1)
                values.add(value.toDouble())
                name = name.replaceFirst(value, "$")
            }
            StatusType.fromDisplay(entry.icon, name)?.let {
                return it.createTimer(entry, values, worldTime)
            }
            return null
        }
    }
}