package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.events.ModTickEvent
import com.calmwolfs.bedwar.events.WorldChangeEvent
import com.calmwolfs.bedwar.events.bedwars.EndGameEvent
import com.calmwolfs.bedwar.events.bedwars.StartGameEvent
import com.calmwolfs.bedwar.events.bedwars.TeamEliminatedEvent
import com.calmwolfs.bedwar.features.session.SessionDisplay
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object BedwarsUtils {
    private var bedwarsArea = false
    private var bedwarsQueue = false
    private var bedwarsGame = false
    private var gameOngoing = false

    private var tabStatPattern =
        "Kills: (?<kills>\\d+) Final Kills: (?<finals>\\d+) Beds Broken: (?<beds>\\d+)".toPattern()
    private var scoreboardTeamPattern = "\\w.(?<team>\\w+):\\s.+\\sYOU".toPattern()

    // todo deal with swap gamemode tbh could just check scoreboard each tick for some stuff and fix rejoins as well
    var currentTeam = ""

    val inBedwarsArea get() = bedwarsArea && Minecraft.getMinecraft().thePlayer != null
    val inBedwarsQueue get() = inBedwarsArea && bedwarsQueue
    val inBedwarsGame get() = inBedwarsArea && bedwarsGame
    val inBedwarsLobby get() = inBedwarsArea && !bedwarsGame && !bedwarsQueue
    val playingBedwars get() = inBedwarsGame && gameOngoing

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
            tabStatPattern.matchMatcher(line.unformat()) {
                SessionDisplay.currentKills = group("kills").toInt()
                SessionDisplay.currentFinals = group("finals").toInt()
                SessionDisplay.currentBeds = group("beds").toInt()
                bedwarsGame = true
            }
        }
    }

    // todo rejoin stuff not breaking

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        gameOngoing = true
        currentTeam = ""
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (inBedwarsGame && currentTeam == "") {
            for (line in ScoreboardData.scoreboard) {
                if (currentTeam != "") continue
                scoreboardTeamPattern.matchMatcher(line.unformat()) {
                    currentTeam = group("team")
                }
            }
        }
    }

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        gameOngoing = false
    }

    @SubscribeEvent
    fun onTeamEliminated(event: TeamEliminatedEvent) {
        if (event.team == currentTeam) {
            gameOngoing = false
        }
    }

    private fun checkScoreboard(): Boolean {
        return ScoreboardData.objectiveLine.unformat().contains("BED WARS")
    }
}