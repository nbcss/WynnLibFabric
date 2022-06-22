package io.github.nbcss.wynnlib.data

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