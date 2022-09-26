package io.github.nbcss.wynnlib.utils.range

import io.github.nbcss.wynnlib.data.Identification
import kotlin.math.*

class BaseIRange(private val id: Identification,
                 private val identified: Boolean,
                 private val base: Int): IRange {

    fun base(): Int = base

    fun getIdentification(): Identification = id

    fun isIdentified(): Boolean = identified

    fun getDistribution() {
        //todo
    }

    override fun lower(): Int {
        return when{
            identified -> base
            id.inverted -> id.range.getUpper(base)
            else -> id.range.getLower(base)
        }
    }

    override fun upper(): Int {
        return when {
            identified -> base
            id.inverted -> id.range.getLower(base)
            else -> id.range.getUpper(base)
        }
    }
}