package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class IDConvertorProperty(ability: Ability,
                          pairs: List<Pair<String, Int>>,
                          private val targetId: String?,
                          private val convertRate: Int,
                          private val maxValue: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<IDConvertorProperty> {
        private const val SOURCE_KEY = "source"
        private const val TARGET_KEY = "target"
        private const val RATE_KEY = "rate"
        private const val MAX_KEY = "max"
        override fun create(ability: Ability, data: JsonElement): IDConvertorProperty {
            val json = data.asJsonObject
            val values: MutableList<Pair<String, Int>> = mutableListOf()
            val source = if (json.has(SOURCE_KEY)) json[SOURCE_KEY].asJsonObject else JsonObject()
            for (entry in source.entrySet()) {
                val value = entry.value.asInt
                if (value != 0)
                    values.add(entry.key to value)
            }
            val targetId = if (json.has(TARGET_KEY)) json[TARGET_KEY].asString else null
            val rate = if (json.has(RATE_KEY)) json[RATE_KEY].asInt else 0
            val maxValue = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
            return IDConvertorProperty(ability, values, targetId, rate, maxValue)
        }
        override fun getKey(): String = "id_convertor"
    }
    private val sources: Map<String, Int> = mapOf(pairs = pairs.toTypedArray())

    override fun writePlaceholder(container: PlaceholderContainer) {
        for (entry in sources.entries) {
            val key = "id_convertor.source.${entry.key}"
            container.putPlaceholder(key, entry.value.toString())
        }
        container.putPlaceholder("id_convertor.max", maxValue.toString())
        container.putPlaceholder("id_convertor.rate", convertRate.toString())
    }

    override fun getOverviewTip(): Text? {
        //println(targetId)
        if (targetId != null) {
            Identification.fromName(targetId)?.let {
                return LiteralText("+ ").formatted(Formatting.GREEN)
                    .append(it.translate(Formatting.GRAY))
            }
        }
        return null
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}