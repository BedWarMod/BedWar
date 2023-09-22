package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.events.ModTickEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object HypixelUtils {
    private var hypixelMain = false
    private var hypixelAlpha = false


    val onHypixel get() = (hypixelMain || hypixelAlpha) && Minecraft.getMinecraft().thePlayer != null

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        hypixelMain = false
        hypixelAlpha = false
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (!event.isMod(5)) return

        if (!onHypixel) {
            checkHypixel()
        }
    }

    private fun checkHypixel() {
        val list = ScoreboardData.scoreboard
        if (list.isEmpty()) return

        val last = list.last()
        hypixelMain = last == "§ewww.hypixel.net"
        hypixelAlpha = last == "§ealpha.hypixel.net"
    }
}