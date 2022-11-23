package io.github.nbcss.wynnlib.timer.status

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.IconTexture
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.timer.IconIndicator
import io.github.nbcss.wynnlib.timer.SideIndicator
import io.github.nbcss.wynnlib.timer.StatusEntry
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.formatTimer
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

abstract class StatusType(data: JsonObject): Keyed, Translatable {
    private val id: String = data["id"].asString
    private val icon: String = data["icon"].asString
    private val name: String = data["name"].asString
    private val text: Translatable? = if (!data["text"].isJsonNull) {
        Translatable.from("wynnlib.indicator.${data["text"].asString.lowercase()}")
    }else{
        null
    }
    private val texture: Identifier? = if (!data["texture"].isJsonNull) {
        IconTexture.fromName(data["texture"].asString).getTexture()
    } else {
        null
    }

    abstract fun renderIcon(
        matrices: MatrixStack,
        textRenderer: TextRenderer,
        timer: TypedStatusTimer,
        icon: Identifier,
        posX: Int, posY: Int, delta: Float
    )

    fun renderText(matrices: MatrixStack,
                   timer: TypedStatusTimer,
                   displayText: Text,
                   posX: Int, posY: Int) {
        val text = LiteralText("")
        val duration: Double? = timer.getDuration()
        if (duration != null) {
            var color = Formatting.GREEN
            if (duration < 10) {
                color = Formatting.RED
            } else if (duration < 30) {
                color = Formatting.GOLD
            }
            text.append(LiteralText(formatTimer((duration * 1000).toLong())).formatted(color))
                .append(" ")
        }
        text.append(displayText)
        RenderKit.renderDefaultOutlineText(matrices, text, posX.toFloat(), posY.toFloat())
    }

    open fun createTimer(entry: StatusEntry, values: List<Double>, worldTime: Long): TypedStatusTimer {
        return TypedStatusTimer(this, values, entry, worldTime)
    }

    fun asSideIndicator(timer: TypedStatusTimer): SideIndicator? {
        return if (text != null) object : SideIndicator {
            override fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int) {
                val args: Array<Any> = timer.getValues().map { removeDecimal(it) }.toTypedArray()
                val displayText = text.formatted(Formatting.GRAY, label = null, args = args)
                renderText(matrices, timer, displayText, posX, posY)
            }
            override fun getDuration(): Double? {
                return timer.getDuration()
            }
        } else null
    }

    fun asIconIndicator(timer: TypedStatusTimer): IconIndicator? {
        return if (texture != null) object : IconIndicator {
            override fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int, delta: Float) {
                renderIcon(matrices, textRenderer, timer, texture, posX, posY, delta)
            }
        } else null
    }


    fun isSideIndicator(): Boolean {
        return text != null
    }

    fun isIconIndicator(): Boolean {
        return texture != null
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.indicator.name.${id.lowercase()}"
    }

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