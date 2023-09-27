package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.events.gui.RenderSlotOverlays
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ItemRenderUtils {
    private val backgroundColour = mutableMapOf<ItemStack, Int>()
    private val backgroundTime = mutableMapOf<ItemStack, Long>()

    var ItemStack.background: Int
        get() {
            if (System.currentTimeMillis() > backgroundTime.getOrDefault(this, 0) + 60) return -1
            return backgroundColour.getOrDefault(this, -1)
        }
        set(value) {
            backgroundColour[this] = value
            backgroundTime[this] = System.currentTimeMillis()
        }

    @SubscribeEvent
    fun renderOverlayLol(event: RenderSlotOverlays) {
        val stack = event.stack ?: return

        val backgroundColor = stack.background
        if (backgroundColor != -1) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(0f, 0f, 110 + Minecraft.getMinecraft().renderItem.zLevel)
            val x = event.x
            val y = event.y
            Gui.drawRect(x, y, x + 16, y + 16, backgroundColor)
            GlStateManager.popMatrix()
        }
    }
}