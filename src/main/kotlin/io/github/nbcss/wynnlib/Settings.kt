package io.github.nbcss.wynnlib

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.gui.ConfigurationScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.ability.AbilityTreeViewerScreen
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.gui.dicts.IngredientDictScreen
import io.github.nbcss.wynnlib.gui.dicts.MaterialDictScreen
import io.github.nbcss.wynnlib.gui.dicts.PowderDictScreen
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.Ingredient
import io.github.nbcss.wynnlib.items.Material
import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.FileUtils
import io.github.nbcss.wynnlib.utils.JsonGetter.getOr
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Scheduler
import net.minecraft.client.MinecraftClient
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import kotlin.concurrent.thread

object Settings {
    private const val PATH = "config/WynnLib/Settings.json"
    private const val PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALolzLdb5Uo2I5x0qy" +
            "LCR7jozU3qwagGsCynYPZe9DEqzB+VRgkS36tWQmctukwH+sZ/uNPOj2F4p7JeggzGAp38vIJTbX+L1vJhy3wJvbWuLlmy/Cw40V" +
            "qWUH6W8DjRubAWGRDYM43p0uwzZPKMwJ1boawPo0xz6eITsmzCLbCZAgMBAAECgYBus9ImbYlT9BSDlEK+finnRtPp9aXLxoZ5V+" +
            "VrP5cbsmcIlV68QLQyEi+VavVzB//UktqZZCuS/Q1GH7xZ2lxtEkrVqtLL1RYS/n1qzu2xZT+ke2FwqP9dtv38VqG1jXwrqYpYc7" +
            "YYswRJVVOZ6FQl9mhBtMfFSvYsJUAYrUXc3QJBAOlS8irh8NKOhZcr4WJSIufgySvdZG5+wJLZLZy9expBoi+/O0glpKLbcGphcg" +
            "Jma8XCse4nPkn/nefu+cEdAwsCQQDMPR2YmqWuSBbmiTyveDwjkqHUHv81+9RbhdmOBjukogL/kQ3YoW7PbhrfTpU81tu9kYXU/l" +
            "L+re3XQQQimyFrAkEAwTqrSa5SZd4YbqitgGKre8Nid0xjd0rLqxHnP26Au67tZYOG0eoy3ZjEEaXf6HLwABiMiMHBSUFDgagc+L" +
            "xRHwJAaDvcqfBrFCo4fcmWjhr33lPMgXycVUnD1D3YjTJDKD+C9jlqbp/c9MJFtqfdZGJnXTUyr0RoyQ+tLclBugOgJwJAaGl6+V" +
            "Un+CEuE3wLbXVoAabgi8orOwtHxh6T0jNxNm0Ji/ICdPplt6Rx+DLO89tpDnd11/PbLYYFpkLNRXd/Fg=="
    private val colorMap: MutableMap<String, Color> = linkedMapOf()
    private val keys: MutableSet<String> = mutableSetOf()
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
    private var isTester: Boolean = false
    private var analysisMode: Boolean = true
    private var dirty: Boolean = false
    private var saving: Boolean = false
    private val defaultTabs: MutableList<TabFactory> = mutableListOf(
        EquipmentDictScreen.FACTORY,
        IngredientDictScreen.FACTORY,
        AbilityTreeViewerScreen.FACTORY,
        PowderDictScreen.FACTORY,
        MaterialDictScreen.FACTORY,
        ConfigurationScreen.FACTORY,
    )

    fun registerTab(factory: TabFactory) {
        defaultTabs.add(factory)
    }

    fun getHandbookTabs(): List<TabFactory> = defaultTabs

    fun reload() {
        FileUtils.readFile(PATH)?.let {
            options.clear()
            for (option in SettingOption.values()) {
                options[option] = getOr(it, option.id, option.defaultValue)
            }
            MatcherType.reload(getOr(it, "matchers", JsonObject()) { x -> x.asJsonObject })
            lockedSlots.clear()
            lockedSlots.addAll(getOr(it, "locked", emptyList()){ i -> i.asInt })
            keys.clear()
            keys.addAll(getOr(it, "keys", emptyList()){ i -> i.asString })
            validateKeys()
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
                data.add("matchers", MatcherType.getData())
                val locked = JsonArray()
                lockedSlots.forEach { locked.add(it) }
                data.add("locked", locked)
                val keys = JsonArray()
                this.keys.forEach { keys.add(it) }
                data.add("keys", keys)
                FileUtils.writeFile(PATH, data)
                dirty = false
                saving = false
            }
        }
    }

    private fun validateKeys() {
        isTester = false
        if (keys.isEmpty()) return
        val id: String = MinecraftClient.getInstance().session.uuid
        try {
            val decoded: ByteArray = Base64.getDecoder().decode(PRIVATE_KEY)
            val priKey = KeyFactory.getInstance("RSA")
                .generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.DECRYPT_MODE, priKey)
            for (key in keys) {
                try {
                    val decrypt = String(cipher.doFinal(Base64.getDecoder().decode(key.toByteArray())))
                    if (decrypt == id) {
                        isTester = true
                        break
                    }
                } catch (ignored: Exception) { }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addKey(key: String): Boolean {
        val id: String = MinecraftClient.getInstance().session.uuid
        try {
            val decoded: ByteArray = Base64.getDecoder().decode(PRIVATE_KEY)
            val priKey = KeyFactory.getInstance("RSA")
                .generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.DECRYPT_MODE, priKey)
            val decrypt = String(cipher.doFinal(Base64.getDecoder().decode(key.toByteArray())))
            if (decrypt == id) {
                isTester = true
                if (keys.add(key)) {
                    markDirty()
                }
                return true
            }
        } catch (e: Exception) { }
        return false
    }

    fun isTester(): Boolean = isTester

    fun setSlotLocked(id: Int, locked: Boolean) {
        if (locked) {
            lockedSlots.add(id)
        }else{
            lockedSlots.remove(id)
        }
        markDirty()
    }

    fun isSlotLocked(id: Int): Boolean {
        return id in lockedSlots
    }

    fun setOption(option: SettingOption, value: Boolean) {
        options[option] = value
        markDirty()
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

    fun markDirty() {
        dirty = true
    }

    enum class SettingOption(val id: String,
                             val defaultValue: Boolean): Keyed, Translatable {
        DURABILITY("durability", true),
        CONSUMABLE_CHARGE("consumable_charge", true),
        SP_VALUE("skill_point_override", true),
        ITEM_BACKGROUND_COLOR("item_color", true),
        LOCK_POUCH_IN_CHEST("pouch_lock", true),
        MAJOR_ID_ANALYZE("major_id_analyze", true),
        ;

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