package com.calmwolfs.bedwar.utils.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.OpenGlHelper


object GuiRenderUtils {
    //From NEU
    fun drawAlphaRectangle(posX: Int, posY: Int, width: Int, height: Int, transparency: Double = 0.9) {
        var alpha = (transparency.coerceIn(0.0, 1.0) * 255).toInt() shl 24

        if (OpenGlHelper.isFramebufferEnabled()) {
            //todo blurred background from neu
        } else {
            alpha = 0xff000000.toInt()
        }

        val background = alpha or 0x202026
        val light = alpha or 0x303036
        val dark = alpha or 0x101016
        val shadow = alpha or 0x000000

        Gui.drawRect(posX, posY, posX + 1, posY + height, light) // Left
        Gui.drawRect(posX + 1, posY, posX + width, posY + 1, light) // Top
        Gui.drawRect(posX + width - 1, posY + 1, posX + width, posY + height, dark) // Right
        Gui.drawRect(posX + 1, posY + height - 1, posX + width - 1, posY + height, dark) // Bottom
        Gui.drawRect(posX + 1, posY + 1, posX + width - 1, posY + height - 1, background) // Middle

        Gui.drawRect(posX + width, posY + 2, posX + width + 2, posY + height + 2, shadow) // Right shadow
        Gui.drawRect(posX + 2, posY + height, posX + width, posY + height + 2, shadow) // Bottom shadow
    }

    fun drawString(str: String, x: Int, y: Int) {
        Minecraft.getMinecraft().fontRendererObj.drawString(str, x.toFloat(), y.toFloat(), 0xffffff, true)
    }
}