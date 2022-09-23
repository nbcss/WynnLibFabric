package io.github.nbcss.wynnlib.matcher

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.types.IngredientTierMatcher
import io.github.nbcss.wynnlib.matcher.types.ItemTierMatcher
import io.github.nbcss.wynnlib.matcher.types.PowderTierMatcher
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.JsonGetter.getOr
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

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
                register(ItemTierMatcher(tier))
            }
            for (tier in Ingredient.Tier.values()) {
                register(IngredientTierMatcher(tier))
            }
            for (tier in Powder.Tier.values()){
                register(PowderTierMatcher(tier))
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
            return typeMap[ItemTierMatcher.keyOf(tier)]!!
        }

        fun fromIngredientTier(tier: Ingredient.Tier): MatcherType {
            return typeMap[IngredientTierMatcher.keyOf(tier)]!!
        }

        fun fromPowderTier(tier: Powder.Tier): MatcherType {
            return typeMap[PowderTierMatcher.keyOf(tier)]!!
        }

        fun getTypes(): List<MatcherType> {
            return typeMap.values.toList()
        }
    }
}