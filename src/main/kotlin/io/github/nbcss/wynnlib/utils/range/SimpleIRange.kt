package io.github.nbcss.wynnlib.utils.range

data class SimpleIRange(private val lower: Int, private val upper: Int): IRange {
    override fun lower(): Int = lower
    override fun upper(): Int = upper
    companion object {
        fun fromString(s: String): IRange {
            val values = s.split("-")
            val min = values[0].toIntOrNull() ?: 0
            val max = if (values.size > 1) values[1].toIntOrNull() ?: min else min
            return SimpleIRange(min, max)
        }
    }
}