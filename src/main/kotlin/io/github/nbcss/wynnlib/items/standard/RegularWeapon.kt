package io.github.nbcss.wynnlib.items.standard

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.utils.asRange

class RegularWeapon(json: JsonObject): Weapon {
    private val damage: IntRange
    private val atkSpeed: AttackSpeed
    init {
        damage = asRange(json.get("damage").asString)
        atkSpeed = Metadata.asAttackSpeed(json.get("attackSpeed").asString)!!
    }

    override fun getDamage(): IntRange = damage

    override fun getElementDamage(elem: Element): IntRange {
        TODO("Not yet implemented")
    }

    override fun getAttackSpeed(): AttackSpeed = atkSpeed
}