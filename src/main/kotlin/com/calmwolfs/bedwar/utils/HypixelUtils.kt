package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.events.HypixelJoinEvent
import com.calmwolfs.bedwar.events.game.ModTickEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object HypixelUtils {
    private var hypixelMain = false
    private var hypixelAlpha = false
    var currentName = ""
    // todo deal with nicks
    private var currentNick = ""

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
            if (onHypixel) {
                HypixelJoinEvent().postAndCatch()
            }
        }
        if (!onHypixel) return

        BedwarsUtils.bedwarsCheck()
    }

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        currentName = Minecraft.getMinecraft().thePlayer.name
    }

    private fun checkHypixel() {
        val list = ScoreboardData.scoreboard
        if (list.isEmpty()) return

        val last = list.last()
        hypixelMain = last == "§ewww.hypixel.net"
        hypixelAlpha = last == "§ealpha.hypixel.net"
    }
}