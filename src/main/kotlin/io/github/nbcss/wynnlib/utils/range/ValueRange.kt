package io.github.nbcss.wynnlib.utils.range

interface ValueRange<T> where T : Number, T: Comparable<T> {
    fun lower(): T
    fun upper(): T
    fun isZero(): Boolean = upper() == 0 && lower() == 0
    fun within(value: T): Boolean = value >= lower() && value <= upper()
    fun isConstant(): Boolean = upper() == lower()
}