package io.github.nbcss.wynnlib.items.identity

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.utils.range.IRange

interface IdentificationHolder: IdPropertyProvider {
    /**
     * Get the roll range of the given identification.
     *
     * @param id: the identification to query
     * @return the range of the given identification
     */
    fun getIdentificationRange(id: Identification): IRange

    /**
     * Get the list of all major ids the item have.
     */
    fun getMajorIds(): List<MajorId> = emptyList()
}