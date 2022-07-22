package io.github.nbcss.wynnlib.abilities

import net.minecraft.util.Identifier

enum class AbilityIcon {
    UNKNOWN,
    //Archer
    ARCHER_MAIN_ATTACK,
    ARROW_STORM,
    ESCAPE,
    ARROW_BOMB,
    ARROW_SHIELD,
    ARROW_RAIN,
    ESCAPE_ARTIST,
    FIERCE_STOMP,
    LEAP,
    SHRAPNEL_BOMB,
    WINDY_FEET,
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
    TANGLED_TRAPS,
    //Mage
    HEAL,
    TELEPORT,
    METEOR,
    ICE_SNAKE, 
    OPHANIM,
    ARCANE_TRANSFER,
    TIMELOCK;
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