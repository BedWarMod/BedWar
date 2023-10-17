package com.calmwolfs.bedwar.utils.gui

import com.calmwolfs.bedwar.config.gui.GuiEditorManager
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsX
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsY
import com.calmwolfs.bedwar.config.gui.Position
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.computer.MouseUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

object GuiElementUtils {
    private fun Position.renderLine(line: List<Any?>, offsetY: Int, itemScale: Double = 1.0): Int {
        GlStateManager.pushMatrix()
        val (x, y) = transform()
        GlStateManager.translate(0f, offsetY.toFloat(), 0F)
        var offsetX = 0
        Renderable.withMousePosition(x, y) {
            for (any in line) {
                val renderable = Renderable.fromAny(any, itemScale = itemScale)
                    ?: throw RuntimeException("Unknown render object: $any")
                renderable.render(offsetX, offsetY)
                offsetX += renderable.width
                GlStateManager.translate(renderable.width.toFloat(), 0F, 0F)
            }
        }
        GlStateManager.popMatrix()
        return offsetX
    }

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

    fun Position.renderStringsAndItems(
        list: List<List<Any?>>,
        extraSpace: Int = 0,
        itemScale: Double = 1.0,
        posLabel: String,
    ) {
        if (list.isEmpty()) return

        var offsetY = 0
        var longestX = 0
        try {
            for (line in list) {
                val x = renderLine(line, offsetY, itemScale)
                if (x > longestX) {
                    longestX = x
                }
                offsetY += 10 + extraSpace + 2
            }
        } catch (e: NullPointerException) {
            for (innerList in list) {
                println("new inner list:")
                for (any in innerList) {
                    println("any: '$any'")
                }
            }
            e.printStackTrace()
            ModUtils.error("Error in renderStringsAndItems!")
        }
        GuiEditorManager.add(this, posLabel, longestX, offsetY)
    }

    fun Position.transform(): Pair<Int, Int> {
        GlStateManager.translate(getAbsX().toFloat(), getAbsY().toFloat(), 0F)
        GlStateManager.scale(effectiveScale, effectiveScale, 1F)
        val x = ((MouseUtils.getMouseX() - getAbsX()) / effectiveScale).toInt()
        val y = ((MouseUtils.getMouseY() - getAbsY()) / effectiveScale).toInt()
        return x to y
    }

    fun ItemStack.renderOnScreen(x: Float, y: Float, scaleMultiplier: Double = 1.0) {
        val isSkull = this.item === Items.skull

        val baseScale = (if (isSkull) 0.8f else 0.6f)
        val finalScale = baseScale * scaleMultiplier
        val diff = ((finalScale - baseScale) * 10).toFloat()

        val translateX: Float
        val translateY: Float
        if (isSkull) {
            translateX = x - 2 - diff
            translateY = y - 2 - diff
        } else {
            translateX = x - diff
            translateY = y - diff
        }

        GlStateManager.pushMatrix()

        GlStateManager.translate(translateX, translateY, 1F)
        GlStateManager.scale(finalScale, finalScale, 1.0)

        RenderHelper.enableGUIStandardItemLighting()
        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(this, 0, 0)
        RenderHelper.disableStandardItemLighting()

        GlStateManager.popMatrix()
    }

    fun ItemStack.renderOnScreen(x: Int, y: Int, scaleMultiplier: Double = 1.0) {
        this.renderOnScreen(x.toFloat(), y.toFloat(), scaleMultiplier)
    }
}