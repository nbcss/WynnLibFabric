package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment

class EquipmentDictScreen : DictionaryScreen<Equipment>("Equipments") {
    fun test(){
        //AdvancementsScreen
    }

    override fun fetchItems(): Collection<Equipment> {
        return emptyList()
    }
}