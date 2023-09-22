package com.calmwolfs.bedwar.utils.gui

import com.calmwolfs.bedwar.config.gui.GuiEditorManager
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsX
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsY
import com.calmwolfs.bedwar.config.gui.Position
import com.calmwolfs.bedwar.utils.computer.MouseUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager

object GuiElementUtils {

    fun Position.renderString(string: String?, offsetX: Int = 0, offsetY: Int = 0, posLabel: String) {
        if (string == null) return
        if (string == "") return
        val x = renderString0(string, offsetX, offsetY)
        GuiEditorManager.add(this, posLabel, x, 10)
    }

    private fun Position.renderString0(string: String?, offsetX: Int = 0, offsetY: Int = 0): Int {
        val display = "§f$string"
        GlStateManager.pushMatrix()
        transform()
        val minecraft = Minecraft.getMinecraft()
        val renderer = minecraft.renderManager.fontRenderer

        val x = offsetX
        val y = offsetY

        GlStateManager.translate(x + 1.0, y + 1.0, 0.0)
        renderer.drawStringWithShadow(display, 0f, 0f, 0)


        GlStateManager.popMatrix()

        return renderer.getStringWidth(display)
    }

    fun Position.transform(): Pair<Int, Int> {
        GlStateManager.translate(getAbsX().toFloat(), getAbsY().toFloat(), 0F)
        GlStateManager.scale(effectiveScale, effectiveScale, 1F)
        val x = ((MouseUtils.getMouseX() - getAbsX()) / effectiveScale).toInt()
        val y = ((MouseUtils.getMouseY() - getAbsY()) / effectiveScale).toInt()
        return x to y
    }
}