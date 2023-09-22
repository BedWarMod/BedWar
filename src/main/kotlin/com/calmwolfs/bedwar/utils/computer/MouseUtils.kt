package com.calmwolfs.bedwar.utils.computer

import net.minecraft.client.Minecraft
import org.lwjgl.input.Mouse

object MouseUtils {
    fun getMouseY(): Int {
        val height = ScreenUtils.mcGuiScale().scaledHeight
        return height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1
    }

    fun getMouseX(): Int = Mouse.getX() * ScreenUtils.mcGuiScale().scaledWidth / Minecraft.getMinecraft().displayWidth
}