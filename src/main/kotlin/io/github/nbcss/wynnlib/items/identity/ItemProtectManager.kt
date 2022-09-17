package io.github.nbcss.wynnlib.items.identity

import io.github.nbcss.wynnlib.registry.StoreSet

object ItemProtectManager {
    private val storage: StoreSet<String> = StoreSet(
        "PROTECT",
        "config/WynnLib/ProtectedItems.json",
        StoreSet.StringConvertor
    )

    fun load() {
        storage.load()
    }

    fun isProtected(item: ConfigurableItem): Boolean {
        return storage.has(item.getConfigKey())
    }

    fun setProtected(item: ConfigurableItem, protected: Boolean) {
        if (protected) {
            storage.add(item.getConfigKey())
        }else{
            storage.remove(item.getConfigKey())
        }
    }
}