package io.github.nbcss.wynnlib.render

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.utils.AlphaColor
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexConsumerProvider.Immediate
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sqrt

object RenderKit {
    private val textRender = MinecraftClient.getInstance().textRenderer
    fun renderTexture(matrices: MatrixStack?,
                      texture: Identifier,
                      x: Int,
                      y: Int,
                      u: Int,
                      v: Int,
                      width: Int,
                      height: Int) {
        renderTexture(matrices, texture, x, y, u, v, width, height, 256, 256)
    }

    fun renderTexture(matrices: MatrixStack,
                      texture: Identifier,
                      x: Double,
                      y: Double,
                      u: Int,
                      v: Int,
                      width: Int,
                      height: Int,
                      texWidth: Int,
                      texHeight: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, texture)
        matrices.push()
        matrices.translate(x, y, 0.0)
        DrawableHelper.drawTexture(matrices, 0, 0, u.toFloat(), v.toFloat(), width, height, texWidth, texHeight)
        matrices.pop()
    }

    fun renderTexture(matrices: MatrixStack?,
                      texture: Identifier,
                      x: Int,
                      y: Int,
                      u: Int,
                      v: Int,
                      width: Int,
                      height: Int,
                      texWidth: Int,
                      texHeight: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, texture)
        DrawableHelper.drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), width, height, texWidth, texHeight)
    }

    fun renderAnimatedTexture(matrices: MatrixStack,
                              texture: Identifier,
                              x: Int,
                              y: Int,
                              width: Int,
                              height: Int,
                              frames: Int,
                              intervalTime: Long = 50,
                              slackTime: Long = 0) {
        val duration = frames * intervalTime + slackTime
        val time = System.currentTimeMillis() % duration
        val index = min((time / intervalTime).toInt(), frames - 1)
        val v = (index * height).toFloat()
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, texture)
        DrawableHelper.drawTexture(matrices, x, y, 0.0f, v, width, height, width, frames * height)
    }

    fun renderTextureWithColor(matrices: MatrixStack,
                               texture: Identifier,
                               color: AlphaColor,
                               x: Int,
                               y: Int,
                               u: Int,
                               v: Int,
                               width: Int,
                               height: Int,
                               texWidth: Int,
                               texHeight: Int) {
        //RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
        //println(texture)
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(color.floatRed(), color.floatGreen(), color.floatBlue(), color.floatAlpha())
        RenderSystem.setShaderTexture(0, texture)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        DrawableHelper.drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), width, height, texWidth, texHeight)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun renderOutlineText(matrices: MatrixStack, text: String, x: Float, y: Float,
                          color: Color = Color.WHITE,
                          outlineColor: Color = Color.BLACK){
        renderOutlineText(matrices, LiteralText(text), x, y, color, outlineColor)
    }

    fun renderDefaultOutlineText(matrices: MatrixStack, text: Text, x: Float, y: Float) {
        renderOutlineText(matrices, text, x, y)
    }

    fun renderOutlineText(matrices: MatrixStack, text: Text, x: Float, y: Float,
                          color: Color = Color.WHITE,
                          outlineColor: Color = Color.BLACK) {
        val immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().buffer)
        textRender.drawWithOutline(text.asOrderedText(), x, y,
            color.code(), outlineColor.code(),
            matrices.peek().positionMatrix, immediate, 15728880)
        immediate.draw()
    }

    fun renderWayPointText(
        matrices: MatrixStack,
        texts: List<Text>,
        worldX: Double,
        worldY: Double,
        worldZ: Double,
        showDistance: Boolean) {
        val client = MinecraftClient.getInstance()
        val camera = client.gameRenderer.camera
        val dx = worldX - camera.pos.x
        val dy = worldY - camera.pos.y
        val dz = worldZ - camera.pos.z
        val distance = sqrt((dx * dx + dy * dy + dz * dz)).toFloat()
        var zoom = 1.0f
        val radius = 10
        if (distance > radius) {
            zoom = distance / radius
        }
        matrices.push()
        matrices.translate(dx, dy, dz)
        matrices.multiply(camera.rotation)
        matrices.scale(-0.025f, -0.025f, 0.025f)
        matrices.scale(zoom, zoom, 1.0f)
        RenderSystem.enableDepthTest()
        val textX = texts.minOf { (-textRender.getWidth(it) / 2).toFloat() }
        var textY = -(texts.size + if (showDistance) 1 else 0) * 10 / 2.0f
        val consumerProvider: Immediate = client.bufferBuilders.entityVertexConsumers
        val matrix4f = matrices.peek().positionMatrix
        for (text in texts) {
            textRender.draw(text, textX, textY, 0xFFFFFF, true,
                matrix4f, consumerProvider, true, 0, 255)
            textY += 10.0f
        }
        if (showDistance) {
            val distanceText = LiteralText("${round(distance).toInt()}m")
            val distanceX = (-textRender.getWidth(distanceText) / 2).toFloat()
            textRender.draw(distanceText, distanceX, textY, 0xFFFFFF, true,
                matrix4f, consumerProvider, true, 0, 255)
        }
        matrices.pop()
    }
}