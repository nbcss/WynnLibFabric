package io.github.nbcss.wynnlib.utils

class IRange(val start: Int, val end: Int) {
    fun isZero(): Boolean = start == 0 && end == 0
}