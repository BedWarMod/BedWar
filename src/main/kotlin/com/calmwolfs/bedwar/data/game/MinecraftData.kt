package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.ModTickEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class MinecraftData {
    private var tick = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        Minecraft.getMinecraft().thePlayer ?: return
        tick++
        ModTickEvent(tick).postAndCatch()
    }
}