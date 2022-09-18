package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.registry.IngredientRegistry
import io.github.nbcss.wynnlib.registry.PowderRegistry
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry

object ItemAPI {
    fun fromId(id: String): BaseItem? {
        var item: BaseItem?
        item = RegularEquipmentRegistry.get(id)
        if (item != null) return item
        item = IngredientRegistry.get(id)
        if (item != null) return item
        item = PowderRegistry.get(id)
        if (item != null) return item
        return item
    }
}