package com.calmwolfs.bedwar.utils.gui

import com.calmwolfs.bedwar.utils.gui.GuiElementUtils.renderOnScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

interface Renderable {
    val width: Int
    val height: Int

    fun render(posX: Int, posY: Int)

    companion object {
        private var currentRenderPassMousePosition: Pair<Int, Int>? = null

        fun <T> withMousePosition(posX: Int, posY: Int, block: () -> T): T {
            val last = currentRenderPassMousePosition
            try {
                currentRenderPassMousePosition = Pair(posX, posY)
                return block()
            } finally {
                currentRenderPassMousePosition = last
            }
        }

        fun fromAny(any: Any?, itemScale: Double = 1.0): Renderable? = when (any) {
            null -> placeholder(12)
            is Renderable -> any
            is String -> string(any)
            is ItemStack -> itemStack(any, itemScale)
            else -> null
        }

        fun itemStack(any: ItemStack, scale: Double = 1.0) = object : Renderable {
            override val width: Int
                get() = 12
            override val height = 10

            override fun render(posX: Int, posY: Int) {
                GlStateManager.pushMatrix()
                if (Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen is GuiChat)
                    GlStateManager.translate(0F, 0F, -145F)
                any.renderOnScreen(0F, 0F, scaleMultiplier = scale)
                GlStateManager.popMatrix()
            }
        }

        fun string(string: String) = object : Renderable {
            override val width: Int
                get() = Minecraft.getMinecraft().fontRendererObj.getStringWidth(string)
            override val height = 10

            override fun render(posX: Int, posY: Int) {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§f$string", 1f, 1f, 0)
            }
        }

        private fun placeholder(width: Int) = object : Renderable {
            override val width: Int = width
            override val height = 10

            override fun render(posX: Int, posY: Int) {
            }
        }
    }
}