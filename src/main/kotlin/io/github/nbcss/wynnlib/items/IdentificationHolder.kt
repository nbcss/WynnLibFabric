package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.utils.range.IRange

interface IdentificationHolder {
    /**
     * Get the roll range of the given identification.
     *
     * @param id: the identification to query
     * @return the range of the given identification
     */
    fun getIdentificationRange(id: Identification): IRange
}