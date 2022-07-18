package io.github.nbcss.wynnlib.items.equipments.analysis

import io.github.nbcss.wynnlib.data.AttackSpeed
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.items.equipments.EquipmentCategory
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class AnalysisWeapon(tooltip: List<Text>): Weapon, EquipmentCategory {
    init {

    }

    override fun getType(): EquipmentType {
        TODO("Not yet implemented")
    }

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getTooltip(): List<Text> {
        TODO("Not yet implemented")
    }

    override fun getDamage(): IRange {
        TODO("Not yet implemented")
    }

    override fun getElementDamage(elem: Element): IRange {
        TODO("Not yet implemented")
    }

    override fun getAttackSpeed(): AttackSpeed {
        TODO("Not yet implemented")
    }
}