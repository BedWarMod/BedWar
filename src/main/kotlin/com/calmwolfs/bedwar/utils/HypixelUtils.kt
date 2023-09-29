package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.HypixelJoinEvent
import com.calmwolfs.bedwar.events.game.ScoreboardUpdateEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object HypixelUtils {
    private var hypixelMain = false
    private var hypixelAlpha = false
    var currentName = ""
    // todo deal with nicks

    val onHypixel get() = (hypixelMain || hypixelAlpha) && Minecraft.getMinecraft().thePlayer != null

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        hypixelMain = false
        hypixelAlpha = false
    }

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        BedWarMod.repo.displayRepoStatus(true)
        currentName = Minecraft.getMinecraft().thePlayer.name
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (event.scoreboard.isEmpty()) return

        if (!onHypixel) {

            val last = event.scoreboard.last()
            hypixelMain = last == "§ewww.hypixel.net"
            hypixelAlpha = last == "§ealpha.hypixel.net"

            if (onHypixel) {
                HypixelJoinEvent().postAndCatch()
            }
        }
        if (!onHypixel) return
    }
}