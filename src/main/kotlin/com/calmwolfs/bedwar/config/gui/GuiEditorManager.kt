package com.calmwolfs.bedwar.config.gui

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.events.game.ModKeyPressEvent
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiEditSign
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.UUID

object GuiEditorManager {
    private var currentPositions = mutableMapOf<String, Position>()
    private var latestPositions = mapOf<String, Position>()
    private var currentBorderSize = mutableMapOf<String, Pair<Int, Int>>()

    @SubscribeEvent
    fun onKeyClick(event: ModKeyPressEvent) {
        Minecraft.getMinecraft().currentScreen?.let {
            if (it !is GuiInventory && it !is GuiChest && it !is GuiEditSign) return
        }

        if (isInGui()) return
        if (event.keyCode == BedWarMod.feature.gui.keyBindOpen) openGuiPositionEditor()
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        latestPositions = currentPositions.toMap()
        currentPositions.clear()
    }

    @JvmStatic
    fun add(position: Position, posLabel: String, x: Int, y: Int) {
        var name = position.internalName
        if (name == null) {
            name = if (posLabel == "none") "none " + UUID.randomUUID() else posLabel
            position.internalName = name
        }
        if (!currentPositions.containsKey(name)) {
            currentPositions[name] = position
            currentBorderSize[posLabel] = Pair(x, y)
        } else {
            val error = Throwable()
            CopyErrorCommand.logError(error, "Duplicate Gui Name Displayed: $name")
        }
    }

    @JvmStatic
    fun openGuiPositionEditor() {
        BedWarMod.screenToOpen = GuiPositionEditor(latestPositions.values.toList(), 2)
    }

    @JvmStatic
    fun renderLast() {
        if (!isInGui()) return

        GlStateManager.translate(0f, 0f, 200f)

        GuiRenderEvent.GuiOverlayRenderEvent().postAndCatch()

        GlStateManager.pushMatrix()
        GlStateManager.enableDepth()
        GuiRenderEvent.ChestGuiOverlayRenderEvent().postAndCatch()
        GlStateManager.popMatrix()

        GlStateManager.translate(0f, 0f, -200f)
    }

    fun isInGui() = Minecraft.getMinecraft().currentScreen is GuiPositionEditor

    fun Position.getDummySize(random: Boolean = false): Vector2 {
        if (random) return Vector2(5, 5)
        val (x, y) = currentBorderSize[internalName] ?: return Vector2(1, 1)
        return Vector2((x * effectiveScale).toInt(), (y * effectiveScale).toInt())
    }

    fun Position.getAbsX() = getAbsX0(getDummySize(true).x)
    fun Position.getAbsY() = getAbsY0(getDummySize(true).y)
}

class Vector2(val x: Int, val y: Int)