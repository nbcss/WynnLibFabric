package io.github.nbcss.wynnlib.utils.range

interface ValueRange<T: Number> {
    fun lower(): T
    fun upper(): T
    fun isZero(): Boolean = upper() == 0 && lower() == 0
    fun isConstant(): Boolean = upper() == lower()
}