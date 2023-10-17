package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.events.bedwars.EndGameEvent
import com.calmwolfs.bedwar.events.bedwars.StartGameEvent
import com.calmwolfs.bedwar.events.bedwars.TeamEliminatedEvent
import com.calmwolfs.bedwar.events.game.FooterUpdateEvent
import com.calmwolfs.bedwar.events.game.GameChatEvent
import com.calmwolfs.bedwar.events.game.ScoreboardUpdateEvent
import com.calmwolfs.bedwar.events.game.WorldChangeEvent
import com.calmwolfs.bedwar.features.session.SessionDisplay
import com.calmwolfs.bedwar.features.team.TeamStatus
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpace
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object BedwarsUtils {
    private var bedwarsArea = false
    private var bedwarsQueue = false
    private var bedwarsGame = false
    private var gameOngoing = false

    private val tabStatPattern = "Kills: (?<kills>\\d+) Final Kills: (?<finals>\\d+) Beds Broken: (?<beds>\\d+)".toPattern()
    private val scoreboardTeamPattern = "\\w.(?<team>\\w+):\\s.+\\sYOU".toPattern()
    private val swapTeamPattern = "Your team swapped and you are now: (?<team>.*)".toPattern()

    var currentTeamName = ""

    val inBedwarsArea get() = bedwarsArea && Minecraft.getMinecraft().thePlayer != null
    val inBedwarsQueue get() = inBedwarsArea && bedwarsQueue
    val inBedwarsGame get() = inBedwarsArea && bedwarsGame
    val inBedwarsLobby get() = inBedwarsArea && !bedwarsGame && !bedwarsQueue
    val playingBedwars get() = inBedwarsGame && gameOngoing

    @SubscribeEvent
    fun onChat(event: GameChatEvent) {
        if (!inBedwarsGame) return
        swapTeamPattern.matchMatcher(event.message.unformat().trimWhiteSpace()) {
            currentTeamName = group("team")
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldChangeEvent) {
        bedwarsArea = false
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        bedwarsArea = false
    }

    // todo rejoin stuff not breaking

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        gameOngoing = true
        currentTeamName = ""
    }

    @SubscribeEvent
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        if (!HypixelUtils.onHypixel) return
        bedwarsArea = event.objective.unformat().contains("BED WARS")

        if (!bedwarsArea) return

        bedwarsQueue = false

        for (line in event.scoreboard) {
            if (currentTeamName == "" && inBedwarsGame) {
                scoreboardTeamPattern.matchMatcher(line.unformat()) {
                    currentTeamName = group("team")
                    TeamStatus.getTeammates()
                    return
                }
            }
            if (!inBedwarsQueue) {
                if (line.startsWith("Players: ")) bedwarsQueue = true
            }
        }
    }

    @SubscribeEvent
    fun onFooterUpdate(event: FooterUpdateEvent) {
        if (!bedwarsArea) return

        bedwarsGame = false
        val splitFooter = event.footer.split("\n")
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

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        gameOngoing = false
    }

    @SubscribeEvent
    fun onTeamEliminated(event: TeamEliminatedEvent) {
        if (event.team == currentTeamName) {
            gameOngoing = false
        }
    }
}