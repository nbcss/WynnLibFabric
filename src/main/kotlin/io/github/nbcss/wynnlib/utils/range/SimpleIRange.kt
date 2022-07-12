package io.github.nbcss.wynnlib.utils.range

data class SimpleIRange(private val lower: Int, private val upper: Int): IRange {
    override fun lower(): Int = lower
    override fun upper(): Int = upper
    companion object {
        fun fromString(s: String): IRange {
            val values = s.split("-")
            if (values[0] == "" && values.size > 1) {
                val value = s.toIntOrNull() ?: 0
                return SimpleIRange(value, value)
            }
            val min = values[0].toIntOrNull() ?: 0
            val max = if (values.size > 1) values[1].toIntOrNull() ?: min else min
            return SimpleIRange(min, max)
        }
    }
}