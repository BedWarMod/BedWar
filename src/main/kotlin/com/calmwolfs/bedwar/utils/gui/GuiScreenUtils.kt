package com.calmwolfs.bedwar.utils.gui

import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11

object GuiScreenUtils {
    fun drawStringCentered(str: String?, x: Int, y: Int) {
        val fr = Minecraft.getMinecraft().fontRendererObj
        val strLen = fr.getStringWidth(str)
        val x2 = x - strLen / 2f
        val y2 = y - fr.FONT_HEIGHT / 2f
        GL11.glTranslatef(x2, y2, 0f)
        fr.drawString(str, 0f, 0f, 0xffffff, true)
        GL11.glTranslatef(-x2, -y2, 0f)
    }

    fun isPointInRect(x: Int, y: Int, left: Int, top: Int, width: Int, height: Int): Boolean {
        return left <= x && x < left + width && top <= y && y < top + height
    }
}