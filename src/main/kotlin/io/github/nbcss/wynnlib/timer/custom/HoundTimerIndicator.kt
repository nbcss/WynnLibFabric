package io.github.nbcss.wynnlib.timer.custom

import io.github.nbcss.wynnlib.abilities.IconTexture
import io.github.nbcss.wynnlib.abilities.properties.general.DurationProperty
import io.github.nbcss.wynnlib.events.ArmourStandUpdateEvent
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.timer.ITimer
import io.github.nbcss.wynnlib.timer.IconIndicator
import io.github.nbcss.wynnlib.timer.IndicatorManager
import io.github.nbcss.wynnlib.timer.TimeTracker
import io.github.nbcss.wynnlib.timer.status.StatusType
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.roundToInt

class HoundTimerIndicator(private val entity: ArmorStandEntity,
                          startTime: Long): ITimer, IconIndicator {
    private val duration: Double = AbilityRegistry.get("CALL_OF_THE_HOUND")
        ?.let{ DurationProperty.from(it) }?.getDuration() ?: 60.0
    private val timeTracker: TimeTracker = TimeTracker(startTime, duration.toInt())

    companion object: EventHandler<ArmourStandUpdateEvent> {
        private val HOUND_PATTERN = Pattern.compile("§b(.+)'s?§7 Hound")
        private val TIMER_PATTERN = Pattern.compile("§7(\\d+)s")
        private val client = MinecraftClient.getInstance()
        override fun handle(event: ArmourStandUpdateEvent) {
            event.entity.customName?.let { name ->
                val matcher = HOUND_PATTERN.matcher(name.asString())
                if (matcher.find()) {
                    if (MinecraftClient.getInstance().player?.entityName == matcher.group(1)) {
                        val indicator = HoundTimerIndicator(event.entity, IndicatorManager.getWorldTime())
                        IndicatorManager.registerTimer(indicator)
                    }
                }
            }
        }
    }

    override fun isExpired(): Boolean {
        return client.world!!.getEntityById(entity.id) == null
    }

    override fun updateWorldTime(time: Long) {
        timeTracker.setWorldTime(time)
        val timer = client.world!!.getEntityById(entity.id + 1)
        if (timer != null) {
            timer.customName?.let { name ->
                val matcher = TIMER_PATTERN.matcher(name.asString())
                if (matcher.find()) {
                    val duration = max(0, matcher.group(1).toInt() - 1)
                    timeTracker.updateRemainTime(duration)
                }
            }
        }
    }

    override fun getDuration(): Double {
        return timeTracker.getDuration()
    }

    override fun getFullDuration(): Double {
        return duration
    }

    override fun getKey(): String {
        return "HOUND_${entity.id}"
    }

    override fun asIconIndicator(): IconIndicator {
        return this
    }

    override fun render(matrices: MatrixStack, textRenderer: TextRenderer, posX: Int, posY: Int, delta: Float) {
        val icon = IconTexture.fromName("CALL_OF_THE_HOUND").getTexture()
        RenderKit.renderTexture(
            matrices, StatusType.ICON_BACKGROUND, posX + 3, posY, 0, 256 - 22, 22, 22
        )
        val duration: Double = getDuration()
        val maxDuration: Double = getFullDuration()
        val pct = MathHelper.clamp(duration / maxDuration, 0.0, 1.0)
        val color = Color(MathHelper.hsvToRgb((pct / 3.0).toFloat(), 1.0f, 1.0f))
        val uv = StatusType.pctToUv(pct)
        RenderKit.renderTextureWithColor(
            matrices, StatusType.ICON_BACKGROUND, color.solid(),
            posX + 3, posY, uv.first, uv.second, 22, 22, 256, 256
        )
        var time = duration.roundToInt().toString() + "s"
        if (duration < 9.95) {
            time = String.format("%.1fs", duration)
        }
        val textX: Int = posX + 14 - textRenderer.getWidth(time) / 2
        val textY = posY + 25
        val text: Text = LiteralText(time).formatted(Formatting.LIGHT_PURPLE)
        RenderKit.renderOutlineText(matrices, text, textX.toFloat(), textY.toFloat())
        RenderKit.renderTexture(
            matrices, icon, posX + 5, posY + 2, 0, 0, 18, 18, 18, 18
        )
    }
}