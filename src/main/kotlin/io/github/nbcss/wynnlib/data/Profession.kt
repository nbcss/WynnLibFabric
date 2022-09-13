package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*
import kotlin.collections.LinkedHashMap

enum class Profession(private val type: Type,
                      private val icon: String): Translatable {
    ARMOURING(Type.CRAFTING, "\u24BD"),
    TAILORING(Type.CRAFTING, "\u24BB"),
    WOODWORKING(Type.CRAFTING, "\u24BE"),
    WEAPONSMITHING(Type.CRAFTING, "\u24BC"),
    JEWELING(Type.CRAFTING, "\u24B9"),
    ALCHEMISM(Type.CRAFTING, "\u24C1"),
    COOKING(Type.CRAFTING, "\u24B6"),
    SCRIBING(Type.CRAFTING, "\u24BA"),
    FARMING(Type.GATHERING, "\u24BF"),
    MINING(Type.GATHERING, "\u24B7"),
    WOODCUTTING(Type.GATHERING, "\u24B8"),
    FISHING(Type.GATHERING, "\u24C0");

    companion object {
        private val VALUE_MAP: Map<String, Profession> = mapOf(
            pairs = values().map { it.name.uppercase() to it }.toTypedArray()
        )

        /**
         * Get Profession instance from case-insensitive name.
         *
         * @param name the name of the profession.
         * @return Profession instance for associated name;
         * if there is not a such instance with given name, the method will return null.
         */
        fun fromName(name: String): Profession? = VALUE_MAP[name.uppercase()]
    }

    fun getIconSymbol(): String = icon

    fun getIconText(): Text = LiteralText(getIconSymbol()).formatted(Formatting.WHITE)

    fun getType(): Type = type

    fun getDisplayText(): Text {
        return LiteralText("")
            .append(getIconText())
            .append(" ")
            .append(translate().formatted(Formatting.GRAY))
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.profession.${name.lowercase(Locale.getDefault())}"
    }

    enum class Type {
        CRAFTING,
        GATHERING
    }
}