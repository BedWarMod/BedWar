package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.events.WorldChangeEvent
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object BedwarsUtils {
    private var bedwarsArea = false
    private var bedwarsQueue = false
    private var bedwarsGame = false
    private var isEliminated = false
    private var isDead = false
    private var gameEnded = false

    private var tabStatPattern = "Kills: (?<kills>\\d+) Final Kills: (?<finals>\\d+) Beds Broken: (?<beds>\\d+)".toPattern()

    val inBedwarsArea get() = bedwarsArea && Minecraft.getMinecraft().thePlayer != null
    val inBedwarsQueue get() = bedwarsArea && bedwarsQueue
    val inBedwarsGame get() = bedwarsArea && bedwarsGame
    val inBedwarsLobby get() = bedwarsArea && !bedwarsGame && !bedwarsQueue

    @SubscribeEvent
    fun onWorldChange(event: WorldChangeEvent) {
        bedwarsArea = false
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        bedwarsArea = false
    }

    fun bedwarsCheck() {
        bedwarsArea = checkScoreboard()

        if (!bedwarsArea) return

        bedwarsQueue = false
        for (line in ScoreboardData.scoreboard) {
            if (bedwarsQueue) continue
            if (line.startsWith("Players: ")) bedwarsQueue = true
        }

        bedwarsGame = false
        val splitFooter = TablistData.footer.split("\n")
        for (line in splitFooter) {
            if (bedwarsGame) continue
            val matcher = tabStatPattern.matcher(line.unformat())
            if (matcher.matches()) {
                bedwarsGame = true
            }
        }
    }

    private fun checkScoreboard(): Boolean {
        return ScoreboardData.objectiveLine.unformat().contains("BED WARS")
    }
}