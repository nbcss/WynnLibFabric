package io.github.nbcss.wynnlib.render

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

object RenderKit {
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
                               color: Color,
                               alpha: Int,
                               x: Int,
                               y: Int,
                               u: Int,
                               v: Int,
                               width: Int,
                               height: Int,
                               texWidth: Int,
                               texHeight: Int) {
        //RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
        RenderSystem.setShaderColor(color.floatRed(), color.floatGreen(), color.floatBlue(), Color.normalize(alpha))
        RenderSystem.setShaderTexture(0, texture)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        DrawableHelper.drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), width, height, texWidth, texHeight)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }
}