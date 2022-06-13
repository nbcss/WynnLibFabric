package io.github.nbcss.wynnlib.utils

import java.util.*

class Version(private val value: String): Comparable<Version> {

    fun get(): String {
        return value
    }

    override operator fun compareTo(other: Version): Int {
        val values = value.split("\\.").toTypedArray()
        val array: Array<String> = other.value.split("\\.").toTypedArray()
        return try {
            for (i in 0 until values.size.coerceAtLeast(array.size)) {
                val v1 = if (i >= values.size) 0 else values[i].toInt()
                val v2 = if (i >= array.size) 0 else array[i].toInt()
                val compare = v1.compareTo(v2)
                if (compare != 0) return compare
            }
            0
        } catch (ignore: NumberFormatException) {
            0
        }
    }

    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val version: Version = other as Version
        return value == version.value
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }
}