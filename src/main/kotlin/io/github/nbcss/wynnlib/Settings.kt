package io.github.nbcss.wynnlib

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.JsonGetter.getOr
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Scheduler
import kotlin.collections.LinkedHashMap
import kotlin.concurrent.thread

object Settings {
    private const val PATH = "config/WynnLib/Settings.json"
    private val colorMap: MutableMap<String, Color> = LinkedHashMap()
    init {
        colorMap["tier.mythic"] = Color.DARK_PURPLE
        colorMap["tier.fabled"] = Color.RED
        colorMap["tier.legendary"] = Color.AQUA
        colorMap["tier.rare"] = Color.PINK
        colorMap["tier.unique"] = Color.YELLOW
        colorMap["tier.set"] = Color.GREEN
        colorMap["tier.normal"] = Color.WHITE
        colorMap["tier.crafted"] = Color.DARK_AQUA
        colorMap["ingredient_tier.star_0"] = Color.DARK_GRAY
        colorMap["ingredient_tier.star_1"] = Color.YELLOW
        colorMap["ingredient_tier.star_2"] = Color.PINK
        colorMap["ingredient_tier.star_3"] = Color.AQUA
        colorMap["material_tier.star_1"] = Color.YELLOW
        colorMap["material_tier.star_2"] = Color.PINK
        colorMap["material_tier.star_3"] = Color.AQUA
        colorMap["powder_tier.i"] = Color.WHITE
        colorMap["powder_tier.ii"] = Color.YELLOW
        colorMap["powder_tier.iii"] = Color.PINK
        colorMap["powder_tier.iv"] = Color.AQUA
        colorMap["powder_tier.v"] = Color.RED
        colorMap["powder_tier.vi"] = Color.DARK_PURPLE
    }
    private val lockedSlots: MutableSet<Int> = mutableSetOf()
    private val options: MutableMap<SettingOption, Boolean> = mutableMapOf()
    private var analysisMode: Boolean = true
    private var dirty: Boolean = false
    private var saving: Boolean = false

    fun reload() {
        FileUtils.readFile(PATH)?.let {
            options.clear()
            for (option in SettingOption.values()) {
                options[option] = getOr(it, option.id, option.defaultValue)
            }
            lockedSlots.clear()
            lockedSlots.addAll(getOr(it, "locked", emptyList()){ i -> i.asInt })
        }
        Scheduler.registerTask("SAVE_SETTING", 20){
            save()
        }
    }

    private fun save() {
        if (dirty && !saving){
            saving = true
            thread(isDaemon = true) {
                val data = JsonObject()
                for (option in SettingOption.values()) {
                    data.addProperty(option.id, getOption(option))
                }
                val locked = JsonArray()
                lockedSlots.forEach { locked.add(it) }
                data.add("locked", locked)
                FileUtils.writeFile(PATH, data)
                dirty = false
                saving = false
            }
        }
    }

    fun setSlotLocked(id: Int, locked: Boolean) {
        if (locked) {
            lockedSlots.add(id)
        }else{
            lockedSlots.remove(id)
        }
        dirty = true
    }

    fun isSlotLocked(id: Int): Boolean {
        return id in lockedSlots
    }

    fun setOption(option: SettingOption, value: Boolean) {
        options[option] = value
        dirty = true
    }

    fun getOption(option: SettingOption): Boolean {
        return options.getOrDefault(option, option.defaultValue)
    }

    fun isAnalysisModeEnabled(): Boolean = analysisMode

    fun toggleAnalysisMode() {
        analysisMode = !analysisMode
    }

    fun getPowderColor(powder: Powder): Color {
        return getColor("powder_tier", powder.getTier().name)
    }

    fun getMaterialColor(tier: Material.Tier): Color {
        return getColor("material_tier", tier.name)
    }

    fun getIngredientColor(tier: Ingredient.Tier): Color {
        return getColor("ingredient_tier", tier.name)
    }

    fun getTierColor(tier: Tier): Color{
        return getColor("tier", tier.name)
    }

    fun getColor(prefix: String, label: String): Color {
        val key = "${prefix}.$label".lowercase()
        return colorMap.getOrDefault(key, Color.WHITE)
    }

    enum class SettingOption(val id: String,
                             val defaultValue: Boolean): Keyed, Translatable {
        DURABILITY("DRAW_DURABILITY", true);

        override fun getKey(): String = id

        override fun getTranslationKey(label: String?): String {
            val key = id.lowercase()
            if (label == "desc"){
                return "wynnlib.setting_option.desc.$key"
            }
            return "wynnlib.setting_option.name.$key"
        }
    }
}