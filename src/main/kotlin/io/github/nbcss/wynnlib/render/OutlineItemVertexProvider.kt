package io.github.nbcss.wynnlib.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import java.util.*

object OutlineItemVertexProvider: VertexConsumerProvider {
    private val parent = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
    private val plainDrawer = VertexConsumerProvider.immediate(BufferBuilder(256))
    private var red = 255
    private var green = 255
    private var blue = 255
    private var alpha = 255

    override fun getBuffer(renderLayer: RenderLayer?): VertexConsumer {
        //val bufferBuilder = BufferBuilder(256)
        //bufferBuilder.begin(renderLayer!!.drawMode, renderLayer.vertexFormat)
        val layer = RenderLayer.getOutline(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)

        //return parent.getBuffer(layer)
        //println(renderLayer!!.isOutline)
        if (layer!!.isOutline) {
            val vertexConsumer = plainDrawer.getBuffer(layer)
            vertexConsumer.next()
            return OutlineVertexConsumer(vertexConsumer, red, green, blue, alpha)
        }
        val vertexConsumer = parent.getBuffer(layer)
        val optional = layer.affectedOutline
        //println("presents ${optional.isPresent}")
        if (optional.isPresent) {
            val vertexConsumer2 = plainDrawer.getBuffer(optional.get())
            val outlineVertexConsumer = OutlineVertexConsumer(
                vertexConsumer2,
                red, green, blue, alpha
            )
            //println("union")
            return VertexConsumers.union(outlineVertexConsumer as VertexConsumer, vertexConsumer)
        }
        return vertexConsumer
    }

    fun setColor(red: Int, green: Int, blue: Int, alpha: Int) {
        this.red = red
        this.green = green
        this.blue = blue
        this.alpha = alpha
    }

    fun draw() {
        plainDrawer.draw()
        //parent.draw()
    }

    fun renderItem(stack: ItemStack, x: Int, y: Int) {
        val client = MinecraftClient.getInstance()
        val model = client.itemRenderer.getHeldItemModel(stack, null, null, 0)
        client.textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false)
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
        RenderSystem.enableBlend()
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        val matrixStack = RenderSystem.getModelViewStack()
        matrixStack.push()
        matrixStack.translate(x.toDouble(), y.toDouble(), 200.0)
        matrixStack.translate(8.0, 8.0, 0.0)
        matrixStack.scale(1.0f, -1.0f, 1.0f)
        matrixStack.scale(16.0f, 16.0f, 16.0f)
        RenderSystem.applyModelViewMatrix()
        RenderSystem.disableDepthTest()
        val matrixStack2 = MatrixStack()
        //val immediate =
        //val immediate = this
        //immediate.setColor(red, green, blue, alpha)
        val bl = !model.isSideLit
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting()
        }
        //RenderPhase
        //client.itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack2, immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, model)
        //val layer = RenderLayers.getItemLayer(stack, true)

        /*RenderSystem.disableTexture()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        val buffer = Tessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

        val entry: MatrixStack.Entry = matrixStack.peek()
        val r = 0x66 / 255.0f
        val g = 0xFF / 255.0f
        val b = 0xFF / 255.0f
        val quads = model.getQuads(null, null, Random(114514))
        *//*for (quad in quads) {
            buffer.quad(entry, quad, r, g, b, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV)
        }*//*
        buffer.vertex(x.toDouble(), y.toDouble(), 300.0).color(0x66, 0xFF, 0xFF, 255).next()
        buffer.vertex(x.toDouble(), y.toDouble() + 30, 300.0).color(0x66, 0xFF, 0xFF, 255).next()
        buffer.vertex(x.toDouble() + 30, y.toDouble() + 30, 300.0).color(0x66, 0xFF, 0xFF, 255).next()
        buffer.vertex(x.toDouble() + 30, y.toDouble(), 300.0).color(0x66, 0xFF, 0xFF, 255).next()
        //buffer.end()
        Tessellator.getInstance().draw()
        RenderSystem.enableDepthTest()*/
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting()
        }
        matrixStack.pop()
        RenderSystem.applyModelViewMatrix()
    }

    @Environment(value = EnvType.CLIENT)
    internal class OutlineVertexConsumer(
        private val delegate: VertexConsumer,
        red: Int,
        green: Int,
        blue: Int,
        alpha: Int
    ) :
        FixedColorVertexConsumer() {
        private var x = 0.0
        private var y = 0.0
        private var z = 0.0
        private var u = 0f
        private var v = 0f
        override fun fixedColor(red: Int, green: Int, blue: Int, alpha: Int) {}
        override fun unfixColor() {}
        override fun vertex(x: Double, y: Double, z: Double): VertexConsumer {
            this.x = x
            this.y = y
            this.z = z
            return this
        }

        override fun color(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer {
            return this
        }

        override fun texture(u: Float, v: Float): VertexConsumer {
            this.u = u
            this.v = v
            return this
        }

        override fun overlay(u: Int, v: Int): VertexConsumer {
            return this
        }

        override fun light(u: Int, v: Int): VertexConsumer {
            return this
        }

        override fun normal(x: Float, y: Float, z: Float): VertexConsumer {
            return this
        }

        override fun vertex(
            x: Float,
            y: Float,
            z: Float,
            red: Float,
            green: Float,
            blue: Float,
            alpha: Float,
            u: Float,
            v: Float,
            overlay: Int,
            light: Int,
            normalX: Float,
            normalY: Float,
            normalZ: Float
        ) {
            delegate.vertex(x.toDouble(), y.toDouble(), z.toDouble()).color(fixedRed, fixedGreen, fixedBlue, fixedAlpha)
                .texture(u, v).next()
        }

        override fun next() {
            delegate.vertex(x, y, z).color(fixedRed, fixedGreen, fixedBlue, fixedAlpha).texture(u, v).next()
        }

        init {
            super.fixedColor(red, green, blue, alpha)
        }
    }
}