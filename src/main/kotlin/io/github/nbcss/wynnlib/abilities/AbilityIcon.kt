package io.github.nbcss.wynnlib.abilities

import net.minecraft.util.Identifier

enum class AbilityIcon(icon: String) {
    UNKNOWN("unknown");
    companion object {
        private val iconMap: Map<String, AbilityIcon> = mapOf(
            pairs = values().map { it.name to it }.toTypedArray()
        )

        fun fromName(name: String?): AbilityIcon {
            return if (name == null) UNKNOWN else iconMap[name.uppercase()] ?: UNKNOWN
        }
    }
    private val texture: Identifier = Identifier("wynnlib", "textures/icons/${icon.lowercase()}.png")

    fun getTexture(): Identifier = texture
}