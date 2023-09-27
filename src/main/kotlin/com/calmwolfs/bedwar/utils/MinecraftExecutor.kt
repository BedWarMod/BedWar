package com.calmwolfs.bedwar.utils

import net.minecraft.client.Minecraft
import java.util.concurrent.Executor

object MinecraftExecutor {

    @JvmField
    val OnThread = Executor {
        val mc = Minecraft.getMinecraft()
        if (mc.isCallingFromMinecraftThread) {
            it.run()
        } else {
            Minecraft.getMinecraft().addScheduledTask(it)
        }
    }
}