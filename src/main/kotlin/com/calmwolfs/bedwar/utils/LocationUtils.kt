package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.types.getModVector
import net.minecraft.client.Minecraft

object LocationUtils {
    fun playerLocation() = Minecraft.getMinecraft().thePlayer.getModVector()
}