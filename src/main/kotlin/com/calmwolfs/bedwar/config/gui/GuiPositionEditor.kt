package com.calmwolfs.bedwar.config.gui

import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsX
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getAbsY
import com.calmwolfs.bedwar.config.gui.GuiEditorManager.getDummySize
import com.calmwolfs.bedwar.utils.MinecraftUtils
import com.calmwolfs.bedwar.utils.NumberUtils.round
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import com.calmwolfs.bedwar.utils.gui.GuiScreenUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.io.IOException

class GuiPositionEditor(private val positions: List<Position>, private val border: Int) : GuiScreen() {
    private var grabbedX = 0
    private var grabbedY = 0
    private var clickedPos = -1

    override fun onGuiClosed() {
        super.onGuiClosed()
        clickedPos = -1
    }

    override fun drawScreen(unusedX: Int, unusedY: Int, partialTicks: Float) {
        super.drawScreen(unusedX, unusedY, partialTicks)
        drawDefaultBackground()

        val hoveredPos = renderRectangles()

        renderLabels(hoveredPos)
    }

    private fun renderLabels(hoveredPos: Int) {
        GuiScreenUtils.drawStringCentered("§cBedWar Mod Position Editor", MinecraftUtils.scaledWidth() / 2, 8)

        var displayPos = -1
        if (clickedPos != -1) {
            if (positions[clickedPos].clicked) {
                displayPos = clickedPos
            }
        }
        if (displayPos == -1) {
            displayPos = hoveredPos
        }

        if (displayPos == -1) {
            GuiScreenUtils.drawStringCentered(
                "§eTo edit hidden GUI elements set a key in /bw edit",
                MinecraftUtils.scaledWidth() / 2,
                20
            )
            GuiScreenUtils.drawStringCentered(
                "§ethen click that key while the GUI element is visible",
                MinecraftUtils.scaledWidth() / 2,
                32
            )
            return
        }

        val pos = positions[displayPos]
        val location = "§7x: §e${pos.rawX}§7, y: §e${pos.rawY}§7, scale: §e${pos.scale.round(2)}"
        GuiScreenUtils.drawStringCentered("§b" + pos.internalName, MinecraftUtils.scaledWidth() / 2, 18)
        GuiScreenUtils.drawStringCentered(location, MinecraftUtils.scaledWidth() / 2, 28)
    }

    private fun renderRectangles(): Int {
        var hoveredPos = -1
        GlStateManager.pushMatrix()
        width = MinecraftUtils.scaledWidth()
        height = MinecraftUtils.scaledHeight()
        val mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth
        val mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1
        for ((index, position) in positions.withIndex()) {
            var elementWidth = position.getDummySize(true).x
            var elementHeight = position.getDummySize(true).y
            if (position.clicked) {
                grabbedX += position.moveX(mouseX - grabbedX, elementWidth)
                grabbedY += position.moveY(mouseY - grabbedY, elementHeight)
            }
            val x = position.getAbsX()
            val y = position.getAbsY()

            elementWidth = position.getDummySize().x
            elementHeight = position.getDummySize().y
            drawRect(x - border, y - border, x + elementWidth + border * 2, y + elementHeight + border * 2, -0x7fbfbfc0)

            if (GuiScreenUtils.isPointInRect(
                    mouseX,
                    mouseY,
                    x - border,
                    y - border,
                    elementWidth + border * 2,
                    elementHeight + border * 2
                )
            ) {
                hoveredPos = index
            }
        }
        GlStateManager.popMatrix()
        return hoveredPos
    }

    @Throws(IOException::class)
    override fun mouseClicked(originalX: Int, priginalY: Int, mouseButton: Int) {
        super.mouseClicked(originalX, priginalY, mouseButton)

        if (mouseButton != 0) return

        val mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth
        val mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1
        for (i in positions.indices.reversed()) {
            val position = positions[i]

            val elementWidth = position.getDummySize().x
            val elementHeight = position.getDummySize().y
            val x = position.getAbsX()
            val y = position.getAbsY()

            if (!position.clicked) {
                if (GuiScreenUtils.isPointInRect(
                        mouseX,
                        mouseY,
                        x - border,
                        y - border,
                        elementWidth + border * 2,
                        elementHeight + border * 2
                    )
                ) {
                    clickedPos = i
                    position.clicked = true
                    grabbedX = mouseX
                    grabbedY = mouseY
                    break
                }
            }
        }
    }

    @Throws(IOException::class)
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        if (clickedPos == -1) return
        val position = positions[clickedPos]
        if (position.clicked) return

        val dist = if (KeyboardUtils.isShiftKeyDown()) 10 else 1
        val elementWidth = position.getDummySize(true).x
        val elementHeight = position.getDummySize(true).y
        when (keyCode) {
            Keyboard.KEY_DOWN -> position.moveY(dist, elementHeight)
            Keyboard.KEY_UP -> position.moveY(-dist, elementHeight)
            Keyboard.KEY_LEFT -> position.moveX(-dist, elementWidth)
            Keyboard.KEY_RIGHT -> position.moveX(dist, elementWidth)
            Keyboard.KEY_MINUS -> position.scale -= .1F
            Keyboard.KEY_SUBTRACT -> position.scale -= .1F
            Keyboard.KEY_EQUALS -> position.scale += .1F
            Keyboard.KEY_ADD -> position.scale += .1F
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        for (position in positions) {
            position.clicked = false
        }
    }

    override fun mouseClickMove(originalX: Int, priginalY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        super.mouseClickMove(originalX, priginalY, clickedMouseButton, timeSinceLastClick)

        for (position in positions) {
            if (!position.clicked) continue

            val mouseX = Mouse.getX() * width / Minecraft.getMinecraft().displayWidth
            val mouseY = height - Mouse.getY() * height / Minecraft.getMinecraft().displayHeight - 1
            val elementWidth = position.getDummySize(true).x
            val elementHeight = position.getDummySize(true).y
            grabbedX += position.moveX(mouseX - grabbedX, elementWidth)
            grabbedY += position.moveY(mouseY - grabbedY, elementHeight)
        }
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        val mw = Mouse.getEventDWheel()
        if (mw == 0) return
        val mx = Mouse.getEventX() * width / mc.displayWidth
        val my = height - Mouse.getEventY() * height / mc.displayHeight - 1
        val hovered = positions.firstOrNull { it.clicked }
            ?: positions.lastOrNull {
                val size = it.getDummySize()
                GuiScreenUtils.isPointInRect(
                    mx, my,
                    it.getAbsX() - border, it.getAbsY() - border,
                    size.x + border * 2, size.y + border * 2
                )
            } ?: return
        if (mw < 0)
            hovered.scale -= .1F
        else
            hovered.scale += .1F
    }
}