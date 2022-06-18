package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.utils.range.IRange

interface Weapon {
    /**
     * Get the neutral damage range of the weapon.
     *
     * @return the neutral damage range of the weapon.
     */
    fun getDamage(): IRange

    /**
     * Get the element damage range of the weapon.
     *
     * @param elem: a non-null element key.
     * @return the element damage range of the given element.
     */
    fun getElementDamage(elem: Element): IRange
    
    fun getAttackSpeed(): AttackSpeed
}