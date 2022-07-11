package io.github.nbcss.wynnlib.abilities

import net.minecraft.util.Identifier

enum class AbilityIcon() {
    UNKNOWN,
    OPHANIM,
    ARCANE_TRANSFER,
    TIMELOCK,
    ARROW_BOMB,
    PHANTOM_RAY,
    GUARDIAN_ANGELS,
    GRAPPLING_HOOK,
    HEAL;
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