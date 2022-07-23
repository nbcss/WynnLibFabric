package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

enum class SpellSlot(val key: String,
                     val displayName: String): Translatable {
    SPELL_1("sp1", "1st Spell"){
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey.opposite(), spellKey)
        }
    },
    SPELL_2("sp2", "2nd Spell"){
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey, spellKey)
        }
    },
    SPELL_3("sp3", "3rd Spell"){
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey.opposite(), spellKey.opposite())
        }
    },
    SPELL_4("sp4", "4th Spell"){
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey, spellKey.opposite())
        }
    };

    fun getComboText(character: CharacterClass): Text {
        getClickCombo(character.getSpellKey()).let { combo ->
            return Translations.TOOLTIP_ABILITY_CLICK_COMBO.translate().formatted(Formatting.GOLD)
                .append(": ")
                .append(combo[0].translate().formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD))
                .append(LiteralText("-").formatted(Formatting.WHITE))
                .append(combo[1].translate().formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD))
                .append(LiteralText("-").formatted(Formatting.WHITE))
                .append(combo[2].translate().formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD))
        }
    }

    companion object {
        private val NAME_MAP: Map<String, SpellSlot> = mapOf(
            pairs = values().map { it.name.uppercase() to it }.toTypedArray()
        )
        private val KEY_MAP: Map<String, SpellSlot> = mapOf(
            pairs = values().map { it.key to it }.toTypedArray()
        )

        fun fromName(name: String): SpellSlot? {
            return NAME_MAP[name.uppercase()]
        }

        fun fromKey(key: String): SpellSlot? = KEY_MAP[key]
    }

    abstract fun getClickCombo(spellKey: MouseKey): Array<MouseKey>

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.spell_slot.${key}"
    }
}