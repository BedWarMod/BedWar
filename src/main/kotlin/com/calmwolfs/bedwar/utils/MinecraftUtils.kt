package com.calmwolfs.bedwar.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution

object MinecraftUtils {
    private fun scaledResolution() = ScaledResolution(Minecraft.getMinecraft())

    fun scaleFactor() = scaledResolution().scaleFactor.toDouble()
    fun scaledHeight() = scaledResolution().scaledHeight
    fun scaledWidth() = scaledResolution().scaledWidth
}