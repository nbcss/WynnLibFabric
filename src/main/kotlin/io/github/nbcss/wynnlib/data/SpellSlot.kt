package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translations
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

enum class SpellSlot {
    SPELL_1{
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey.opposite(), spellKey)
        }
    },
    SPELL_2{
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey, spellKey)
        }
    },
    SPELL_3{
        override fun getClickCombo(spellKey: MouseKey): Array<MouseKey> {
            return arrayOf(spellKey, spellKey.opposite(), spellKey.opposite())
        }
    },
    SPELL_4{
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
        private val VALUE_MAP: MutableMap<String, SpellSlot> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.uppercase()] = it }
        }

        fun fromName(name: String): SpellSlot? {
            return VALUE_MAP[name.uppercase()]
        }
    }

    abstract fun getClickCombo(spellKey: MouseKey): Array<MouseKey>
}