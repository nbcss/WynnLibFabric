package io.github.nbcss.wynnlib.matcher

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.Emerald
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.types.*
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.JsonGetter.getOr
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text

interface MatcherType: Keyed, Translatable {

    fun reload(data: JsonObject)
    fun getData(): JsonObject
    fun getColor(): Color
    fun setColor(color: Color)
    fun getDisplayText(): Text

    companion object {
        private val typeMap: MutableMap<String, MatcherType> = linkedMapOf()
        init {
            for (tier in Tier.values()) {
                register(ItemTierType(tier))
            }
            for (tier in Ingredient.Tier.values()) {
                register(IngredientTierType(tier))
            }
            for (tier in Material.Tier.values()) {
                register(MaterialTierType(tier))
            }
            for (tier in Powder.Tier.values()) {
                register(PowderTierType(tier))
            }
            for (tier in Emerald.Tier.values()) {
                register(EmeraldTierType(tier))
            }
        }

        private fun register(type: MatcherType): MatcherType {
            typeMap[type.getKey()] = type
            return type
        }

        fun getData(): JsonObject {
            val data = JsonObject()
            for (entry in typeMap.entries) {
                data.add(entry.key, entry.value.getData())
            }
            return data
        }

        fun reload(data: JsonObject) {
            for (entry in typeMap.entries) {
                val entryData = getOr(data, entry.key, JsonObject()) { it.asJsonObject }
                entry.value.reload(entryData)
            }
        }

        fun fromItemTier(tier: Tier): MatcherType {
            return typeMap[ItemTierType.keyOf(tier)]!!
        }

        fun fromIngredientTier(tier: Ingredient.Tier): MatcherType {
            return typeMap[IngredientTierType.keyOf(tier)]!!
        }

        fun fromMaterialTier(tier: Material.Tier): MatcherType {
            return typeMap[MaterialTierType.keyOf(tier)]!!
        }

        fun fromPowderTier(tier: Powder.Tier): MatcherType {
            return typeMap[PowderTierType.keyOf(tier)]!!
        }

        fun fromEmeraldTier(tier: Emerald.Tier): MatcherType {
            return typeMap[EmeraldTierType.keyOf(tier)]!!
        }

        fun getTypes(): List<MatcherType> {
            return typeMap.values.toList()
        }
    }
}