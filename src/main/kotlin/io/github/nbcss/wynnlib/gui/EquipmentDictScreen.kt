package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment
import net.minecraft.text.TranslatableText

class EquipmentDictScreen : DictionaryScreen<Equipment>(TranslatableText("wynnlib.ui.equipments")) {
    fun test(){
        //AdvancementsScreen
    }

    override fun fetchItems(): Collection<Equipment> {
        return emptyList()
    }
}