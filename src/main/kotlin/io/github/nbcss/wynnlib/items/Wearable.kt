package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.utils.IRange

interface Wearable {
    /**
     * Get the health range of the armour.
     * Note: only crafted armours have health range in game.
     *
     * @return the health range of the armour.
     */
    fun getHealth(): IRange

    /**
     * Get the element defense value of the armour.
     *
     * @param elem: a non-null element key.
     * @return the defense value of the given element.
     */
    fun getElementDefence(elem: Element): Int
}