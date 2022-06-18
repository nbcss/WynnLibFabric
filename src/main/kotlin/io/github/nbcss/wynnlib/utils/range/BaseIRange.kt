package io.github.nbcss.wynnlib.utils.range

import io.github.nbcss.wynnlib.data.Identification
import kotlin.math.*

class BaseIRange(private val id: Identification, private val base: Int): IRange {

    fun base(): Int = base


    private fun getLower(): Int {
        return when {
            base>0 -> max(1, (base*0.3).roundToInt()) // Fix when base is 1 which should return 1 but returns 0
            else -> (base*1.3).roundToInt()
        }
    }

    private fun getUpper(): Int {
        return when {
            base > 0 -> (base * 1.3).roundToInt()
            else -> (base * 0.7).roundToInt()
        }
    }
    override fun lower(): Int {
        return when{
            id.constant -> base
            id.inverted -> getUpper()
            else -> getLower()}
    }

    override fun upper(): Int {
        return when {
            id.constant -> base
            id.inverted -> getLower()
            else -> getUpper()
        }
    }
}