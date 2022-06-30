package io.github.nbcss.wynnlib.utils.range

data class SimpleDRange(private val lower: Double, private val upper: Double): DRange {
    override fun lower(): Double = lower
    override fun upper(): Double = upper
    companion object {
        fun fromString(s: String): DRange {
            val values = s.split("-")
            val min = values[0].toDoubleOrNull() ?: 0.0
            val max = if (values.size > 1) values[1].toDoubleOrNull() ?: min else min
            return SimpleDRange(min, max)
        }
    }
}