package io.github.nbcss.wynnlib.matcher

interface ProtectableType {
    fun isProtected(): Boolean
    fun setProtected(value: Boolean)
}