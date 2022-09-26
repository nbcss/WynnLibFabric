package io.github.nbcss.wynnlib.utils.id

import kotlin.math.max
import kotlin.math.roundToInt

enum class IDRange(val posMin: Int, val posMax: Int, val negMin: Int, val negMax: Int) {
    BASE(30, 130, 130, 70),
    CONSTANT(100, 100, 100, 100),
    SHORT(85, 115, 115, 85);
    companion object {
        private val nameMap: Map<String, IDRange> = mapOf(
            pairs = values().map { it.name to it }.toTypedArray()
        )
        fun fromName(name: String): IDRange {
            return nameMap[name.uppercase()] ?: BASE
        }
    }

    fun getLower(base: Int): Int {
        return if (base > 0) {
            max(1, (base * (posMin / 100.0)).roundToInt())
        }else if(base < 0) {
            (base * (negMin / 100.0)).roundToInt()
        }else{
            0
        }
    }

    fun getUpper(base: Int): Int {
        return if (base > 0) {
            (base * (posMax / 100.0)).roundToInt()
        }else if(base < 0) {
            (base * (negMax / 100.0)).roundToInt()
        }else{
            0
        }
    }
}