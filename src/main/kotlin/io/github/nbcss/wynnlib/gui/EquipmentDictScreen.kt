package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import net.minecraft.text.TranslatableText

class EquipmentDictScreen : DictionaryScreen<Equipment>(TranslatableText("wynnlib.ui.equipments")) {
    fun test(){
        //AdvancementsScreen
    }

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
    }
}