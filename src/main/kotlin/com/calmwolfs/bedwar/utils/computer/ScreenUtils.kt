package com.calmwolfs.bedwar.utils.computer

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution

object ScreenUtils {

    fun mcGuiScale(): ScaledResolution = ScaledResolution(Minecraft.getMinecraft())
}