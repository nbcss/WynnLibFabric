package io.github.nbcss.wynnlib.utils.keys

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil

object KeysKit {
    fun isShiftDown(): Boolean {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(
            MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_RIGHT_SHIFT)
    }

    fun isAltDown(): Boolean {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_LEFT_ALT) || InputUtil.isKeyPressed(
            MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_RIGHT_ALT)
    }

    fun isCtrlDown(): Boolean {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyPressed(
            MinecraftClient.getInstance().window.handle,
            InputUtil.GLFW_KEY_RIGHT_CONTROL)
    }
}