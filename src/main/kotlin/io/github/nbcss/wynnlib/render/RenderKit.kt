package io.github.nbcss.wynnlib.render

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.utils.AlphaColor
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

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
            color.getColorCode(), outlineColor.getColorCode(),
            matrices.peek().positionMatrix, immediate, 15728880)
        immediate.draw()
    }
}