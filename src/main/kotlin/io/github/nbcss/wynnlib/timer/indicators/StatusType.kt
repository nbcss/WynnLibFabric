package io.github.nbcss.wynnlib.timer.indicators

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.IconTexture
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.timer.IconIndicator
import io.github.nbcss.wynnlib.timer.SideIndicator
import io.github.nbcss.wynnlib.timer.StatusEntry
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

abstract class StatusType(data: JsonObject): Keyed {
    private val id: String = data["id"].asString
    private val icon: String = data["icon"].asString
    private val name: String = data["name"].asString
    private val texture: Identifier? = if (!data["texture"].isJsonNull) {
        IconTexture.fromName(data["texture"].asString).getTexture()
    } else {
        null
    }

    abstract fun renderIcon(matrices: MatrixStack,
                            textRenderer: TextRenderer,
                            timer: TypedStatusTimer,
                            icon: Identifier,
                            posX: Int, posY: Int)

    open fun createTimer(entry: StatusEntry, values: List<Int>, worldTime: Long): TypedStatusTimer {
        return TypedStatusTimer(this, values, entry, worldTime)
    }

    fun asSideIndicator(timer: TypedStatusTimer): SideIndicator? {
        return null //todo
    }

    fun asIconIndicator(timer: TypedStatusTimer): IconIndicator? {
        return if (texture != null) object : IconIndicator {
            override fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int) {
                renderIcon(matrices, textRenderer, timer, texture, posX, posY)
            }
        } else null
    }

    override fun getKey(): String = id

    companion object: Registry<StatusType>() {
        val ICON_BACKGROUND: Identifier = Identifier("wynnlib", "textures/hud/timer_background.png")
        private const val RESOURCE = "assets/wynnlib/data/Timers.json"
        private const val UNIT_SIZE = 85
        private val internalMap: MutableMap<String, StatusType> = mutableMapOf()
        private val types: Map<String, Factory> = mapOf(
            pairs = listOf(
                EffectIndicator,
                CooldownIndicator,
                TemporaryEffectIndicator,
                ValuesIndicator,
            ).map { it.getKey() to it }.toTypedArray()
        )

        override fun getFilename(): String = RESOURCE

        override fun read(data: JsonObject): StatusType? {
            return types[data["type"].asString]?.create(data)
        }

        override fun reload(array: JsonArray) {
            internalMap.clear()
            super.reload(array)
        }

        override fun put(item: StatusType) {
            internalMap[toInternalKey(item.icon, item.name)] = item
            super.put(item)
        }

        private fun toInternalKey(icon: String, name: String): String {
            return "$icon@$name"
        }

        fun pctToUv(pct: Double): Pair<Int, Int> {
            val index = ((1 - pct) * UNIT_SIZE).roundToInt()
            val u = index % 11 * 22
            val v = index / 11 * 22
            return u to v
        }

        fun fromDisplay(icon: String, name: String): StatusType? {
            return internalMap[toInternalKey(icon, name)]
        }
    }

    interface Factory: Keyed {
        fun create(data: JsonObject): StatusType
    }
}