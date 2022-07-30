package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.Identification

class IDConvertorProperty(ability: Ability,
                          pairs: List<Pair<Identification, Int>>,
                          private val targetId: Identification?,
                          private val convertRate: Int,
                          private val maxValue: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<IDConvertorProperty> {
        private const val SOURCE_KEY = "source"
        private const val TARGET_KEY = "target"
        private const val RATE_KEY = "rate"
        private const val MAX_KEY = "max"
        override fun create(ability: Ability, data: JsonElement): IDConvertorProperty {
            val json = data.asJsonObject
            val values: MutableList<Pair<Identification, Int>> = mutableListOf()
            val source = if (json.has(SOURCE_KEY)) json[SOURCE_KEY].asJsonObject else JsonObject()
            for (entry in source.entrySet()) {
                Identification.fromName(entry.key)?.let {
                    val value = entry.value.asInt
                    if (value != 0)
                        values.add(it to value)
                }
            }
            val targetId = if (json.has(TARGET_KEY)) Identification.fromName(json[TARGET_KEY].asString) else null
            val rate = if (json.has(RATE_KEY)) json[RATE_KEY].asInt else 0
            val maxValue = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
            return IDConvertorProperty(ability, values, targetId, rate, maxValue)
        }
        override fun getKey(): String = "id_convertor"
    }
    private val sources: Map<Identification, Int> = mapOf(pairs = pairs.toTypedArray())

    override fun writePlaceholder(container: PlaceholderContainer) {
        for (entry in sources.entries) {
            val key = "id_convertor.source.${entry.key.name}"
            container.putPlaceholder(key, entry.value.toString())
        }
        container.putPlaceholder("id_convertor.max", maxValue.toString())
        container.putPlaceholder("id_convertor.rate", convertRate.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}