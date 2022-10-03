package io.github.nbcss.wynnlib.items.identity

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.registry.SavingStorage
import io.github.nbcss.wynnlib.utils.Keyed

interface ConfigurableItem: Keyed {
    fun getConfigDomain(): String
    fun getConfigKey(): String = "${getConfigDomain()}:${getKey()}"

    object Modifier {
        private val registry = object : SavingStorage<ConfigData>() {
            override fun getSavePath(): String = "config/WynnLib/ItemConfig.json"

            override fun getKey(): String = "ConfigItemRegistry"

            override fun getData(item: ConfigData): JsonObject {
                val data = JsonObject()
                data.addProperty("id", item.itemKey)
                data.add("properties", item.properties)
                return data
            }

            override fun read(data: JsonObject): ConfigData? {
                if (!data.has("id") || !data.has("properties"))
                    return null
                val id = data["id"].asString
                val properties = data["properties"].asJsonObject
                return ConfigData(id, properties)
            }
        }

        fun load() {
            registry.load()
        }

        fun write(item: ConfigurableItem, field: String, value: JsonElement) {
            val configData = registry.get(item.getConfigKey()) ?: ConfigData(item.getConfigKey(), JsonObject())
            configData.properties.add(field, value)
            registry.put(configData)
        }

        fun clear(item: ConfigurableItem, field: String) {
            registry.get(item.getConfigKey())?.let {
                if (it.properties.has(field)) {
                    it.properties.remove(field)
                    if (it.isEmpty()) {
                        registry.remove(item.getConfigKey())
                    }else{
                        registry.markDirty()
                    }
                }
            }
        }

        fun read(item: ConfigurableItem, field: String): JsonElement? {
            return registry.get(item.getConfigKey())?.properties?.get(field)?.deepCopy()
        }

        fun has(item: ConfigurableItem, field: String): Boolean {
            return registry.get(item.getConfigKey())?.properties?.has(field) ?: false
        }
    }

    private class ConfigData(val itemKey: String,
                             var properties: JsonObject): Keyed {
        override fun getKey(): String = itemKey
        fun isEmpty(): Boolean = properties.size() == 0
    }
}