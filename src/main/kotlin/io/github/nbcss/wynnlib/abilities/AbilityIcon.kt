package io.github.nbcss.wynnlib.abilities

import net.minecraft.util.Identifier

enum class AbilityIcon() {
    UNKNOWN,
    HEAL,
    TELEPORT,
    METEOR,
    ICE_SNAKE, 
    OPHANIM,
    ARCANE_TRANSFER,
    TIMELOCK,
    ARROW_BOMB,
    PHANTOM_RAY,
    GUARDIAN_ANGELS,
    GRAPPLING_HOOK,
    HEART_SHATTER,
    BRYOPHYTE_ROOTS,
    BASALTIC_TRAP,
    FIRE_CREEP,
    MANA_TRAP,
    GRAPE_BOMB,
    CALL_OF_THE_HOUND,
    TANGLED_TRAPS;
    companion object {
        private val iconMap: Map<String, AbilityIcon> = mapOf(
            pairs = values().map { it.name to it }.toTypedArray()
        )

        fun fromName(name: String?): AbilityIcon {
            return if (name == null) UNKNOWN else iconMap[name.uppercase()] ?: UNKNOWN
        }
    }
    private val texture: Identifier = Identifier("wynnlib", "textures/icons/${name.lowercase()}.png")

    fun getTexture(): Identifier = texture
}