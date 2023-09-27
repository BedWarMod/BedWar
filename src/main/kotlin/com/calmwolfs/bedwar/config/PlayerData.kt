package com.calmwolfs.bedwar.config

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.ConfigLoadEvent
import com.calmwolfs.bedwar.events.HypixelJoinEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PlayerData {
    var playerSpecific: Storage.PlayerSpecific? = null

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        val playerUuid = Minecraft.getMinecraft().thePlayer.uniqueID
        playerSpecific = BedWarMod.feature.storage.players.getOrPut(playerUuid) { Storage.PlayerSpecific() }
        ConfigLoadEvent().postAndCatch()
    }
}