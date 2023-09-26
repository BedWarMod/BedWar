package com.calmwolfs.bedwar.features.session

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.data.types.GameModeData
import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.ListUtils.addAsSingletonList
import com.calmwolfs.bedwar.utils.NumberUtils.getRatio
import com.calmwolfs.bedwar.utils.NumberUtils.round
import com.calmwolfs.bedwar.utils.computer.TimeUtils
import com.calmwolfs.bedwar.utils.gui.GuiElementUtils.renderStringsAndItems
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.concurrent.fixedRateTimer

object SessionDisplay {
    private val config get() = BedWarMod.feature.session.sessionTracker
    private var display = emptyList<List<Any>>()

    var currentKills = 0
    var currentFinals = 0
    var currentBeds = 0

    private var secondsPassed = 0
    private var currentSeconds = 0
    private var averageSeconds = 0

    private var currentlyAlive = false
    private var sessionStats = GameModeData()

    //todo update winstreak from the npc

    init {
        fixedRateTimer(name = "bedwar-session-tracker", period = 1000) {
            try {
                updateDisplay()
            } catch (error: Throwable) {
                CopyErrorCommand.logError(error, "Error updating BedWars session tracker!")
            }
        }
    }

    private fun updateDisplay() {
        if (!BedwarsUtils.inBedwarsArea) return
        if (!BedwarsUtils.inBedwarsGame) {
            currentKills = 0
            currentFinals = 0
            currentBeds = 0
        }
        if (BedwarsUtils.playingBedwars) {
            secondsPassed++
            currentSeconds++
            val sessionGames = sessionStats.wins + sessionStats.losses
            if (sessionGames != 0) {
                averageSeconds = (secondsPassed - currentSeconds) / sessionGames
            }
        }

        display = formatDisplay(drawSessionDisplay())
    }

    private fun drawSessionDisplay() = buildList<List<Any>> {
        val sessionGames = sessionStats.wins + sessionStats.losses
        val kdr = getRatio(sessionStats.kills, sessionStats.deaths)
        val fkdr = getRatio(sessionStats.finalKills, sessionStats.finalDeaths)
        val bblr = getRatio(sessionStats.bedsBroken, sessionStats.bedsLost)
        val wlr = getRatio(sessionStats.wins, sessionStats.losses)
        val winRate = if (sessionStats.wins + sessionStats.losses > 0) {
            ((sessionStats.wins.toDouble() / (sessionStats.wins + sessionStats.losses)) * 100).round(3)
        } else {
            "0.00%"
        }

        addAsSingletonList("§e§lGame")
        addAsSingletonList("Kills: §a$currentKills")
        addAsSingletonList("Finals: §a$currentFinals")
        addAsSingletonList("Beds: §a$currentBeds")
        addAsSingletonList(" ")
        addAsSingletonList("§e§lSession")
        addAsSingletonList("Kills: §a${sessionStats.kills} §7| §fKDR: §a$kdr")
        addAsSingletonList("Finals: §a${sessionStats.finalKills} §7| §fFKDR: §a$fkdr")
        addAsSingletonList("Beds: §a${sessionStats.bedsBroken} §7| §fBBLR: §a$bblr")
        addAsSingletonList("Wins: §a${sessionStats.wins} §7| §fWLR: §a$wlr")
        addAsSingletonList(" ")
        addAsSingletonList("Winstreak: §a${sessionStats.winstreak}")
        addAsSingletonList("Win Rate: §a$winRate%")
        addAsSingletonList("Session Games: §a$sessionGames")
        addAsSingletonList("Session Time: §a${TimeUtils.formatSeconds(secondsPassed)}")
        addAsSingletonList("Game Time: §a${TimeUtils.formatSeconds(currentSeconds)}")
        addAsSingletonList("AVG Game Time: §a${TimeUtils.formatSeconds(averageSeconds)}")
        addAsSingletonList(" ")
    }

    private fun formatDisplay(map: List<List<Any>>): List<List<Any>> {
        val newList = mutableListOf<List<Any>>()
        for (index in config.order) {
            newList.add(map[index])
        }
        return newList
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!config.enabled) return
        if (!BedwarsUtils.inBedwarsArea) return
        if (!config.showInLobby && BedwarsUtils.inBedwarsLobby) return

        config.position.renderStringsAndItems(display, posLabel = "Session Tracker")
    }

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        currentSeconds = 0
        currentlyAlive = false
    }

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        if (!currentlyAlive) return
        if (event.winningTeam == BedwarsUtils.currentTeam) {
            sessionStats.wins++
            sessionStats.winstreak++
        } else {
            sessionStats.losses++
            sessionStats.winstreak = 0
        }
        val sessionGames = sessionStats.wins + sessionStats.losses
        averageSeconds = (secondsPassed - currentSeconds) / sessionGames
        currentlyAlive = false
    }

    @SubscribeEvent
    fun onTeamEliminated(event: TeamEliminatedEvent) {
        if (event.team == BedwarsUtils.currentTeam && currentlyAlive) {
            sessionStats.losses++
            sessionStats.winstreak = 0
            val sessionGames = sessionStats.wins + sessionStats.losses
            averageSeconds = (secondsPassed - currentSeconds) / sessionGames
            currentlyAlive = false
        }
    }

    @SubscribeEvent
    fun onBedBreak(event: BedBreakEvent) {
        if (event.team == BedwarsUtils.currentTeam) {
            sessionStats.bedsLost++
        }
        // todo nicks
        if (event.player == HypixelUtils.currentName) {
            sessionStats.bedsBroken++
        }
    }

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        // todo nicks
        if (event.killer == HypixelUtils.currentName) {
            sessionStats.kills++
        }
        if (event.killed == HypixelUtils.currentName) {
            sessionStats.deaths++
        }
    }

    @SubscribeEvent
    fun onFinalKill(event: FinalKillEvent) {
        // todo nicks
        if (event.killer == HypixelUtils.currentName) {
            sessionStats.finalKills++
        }
        if (event.killed == HypixelUtils.currentName) {
            sessionStats.finalDeaths++
        }
    }

    fun resetTracker() {
        secondsPassed = 0
        currentSeconds = 0
        averageSeconds = 0

        sessionStats = GameModeData()
    }
}