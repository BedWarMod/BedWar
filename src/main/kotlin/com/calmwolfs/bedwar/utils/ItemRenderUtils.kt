package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.events.gui.RenderSlotOverlays
import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.milliseconds

object ItemRenderUtils {
    private val backgroundColour = mutableMapOf<ItemStack, Int>()
    private val backgroundTime = mutableMapOf<ItemStack, SimpleTimeMark>()

    var ItemStack.background: Int
        get() {
            if (backgroundTime.getOrDefault(this, SimpleTimeMark.farPast()).passedSince() > 60.milliseconds) return -1
            return backgroundColour.getOrDefault(this, -1)
        }
        set(value) {
            backgroundColour[this] = value
            backgroundTime[this] = SimpleTimeMark.now()
        }

    @SubscribeEvent
    fun renderOverlayLol(event: RenderSlotOverlays) {
        val stack = event.stack ?: return

        val backgroundColour = stack.background
        if (backgroundColour != -1) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(0f, 0f, 110 + Minecraft.getMinecraft().renderItem.zLevel)
            val x = event.x
            val y = event.y
            Gui.drawRect(x, y, x + 16, y + 16, backgroundColour)
            GlStateManager.popMatrix()
        }
    }
}