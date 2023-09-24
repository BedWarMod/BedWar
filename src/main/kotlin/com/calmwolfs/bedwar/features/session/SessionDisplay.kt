package com.calmwolfs.bedwar.features.session

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.ListUtils.addAsSingletonList
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

    private var sessionGames = 0
    private var sessionWins = 0
    private var sessionLosses = 0

    private var sessionKills = 0
    private var sessionDeaths = 0

    private var sessionFinalKills = 0
    private var sessionFinalDeaths = 0

    private var sessionBedsBroken = 0
    private var sessionBedsLost = 0

    //todo get from the npc
    private var currentWinstreak = 0

    init {
        fixedRateTimer(name = "bedwar-session-tracker", period = 1000) {
            updateDisplay()
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
            if (sessionGames != 0) {
                averageSeconds = (secondsPassed - currentSeconds) / sessionGames
            }
        }

        display = formatDisplay(drawSessionDisplay())
    }

    private fun drawSessionDisplay() = buildList<List<Any>> {
        addAsSingletonList("§e§lGame")
        addAsSingletonList("Kills: §a$currentKills")
        addAsSingletonList("Finals: §a$currentFinals")
        addAsSingletonList("Beds: §a$currentBeds")
        addAsSingletonList(" ")
        addAsSingletonList("§e§lSession")
        addAsSingletonList("Kills: §a$sessionKills §7| §fKDR: §a${getRatio(sessionKills, sessionDeaths)}")
        addAsSingletonList("Finals: §a$sessionFinalKills §7| §fFKDR: §a${getRatio(sessionFinalKills, sessionFinalDeaths)}")
        addAsSingletonList("Beds: §a$sessionBedsBroken §7| §fBBLR: §a${getRatio(sessionBedsBroken, sessionBedsLost)}")
        addAsSingletonList("Wins: §a$sessionWins §7| §fWLR: §a${getRatio(sessionWins, sessionLosses)}")
        addAsSingletonList(" ")
        addAsSingletonList("Winstreak: §a$currentWinstreak")
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
    }

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        if (event.winningTeam == BedwarsUtils.currentTeam) {
            sessionWins++
            currentWinstreak++
        } else {
            sessionLosses++
            currentWinstreak = 0
        }
        sessionGames++
        averageSeconds = (secondsPassed - currentSeconds) / sessionGames
    }

    @SubscribeEvent
    fun onBedBreak(event: BedBreakEvent) {
        if (event.teamBroken == BedwarsUtils.currentTeam) {
            sessionBedsLost++
        }
        // todo nicks
        if (event.playerBreaking == HypixelUtils.currentName) {
            sessionBedsBroken++
        }
    }

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        // todo nicks
        if (event.killer == HypixelUtils.currentName) {
            sessionKills++
        }
        if (event.killed == HypixelUtils.currentName) {
            sessionDeaths++
        }
    }

    @SubscribeEvent
    fun onFinalKill(event: FinalKillEvent) {
        // todo nicks
        if (event.killer == HypixelUtils.currentName) {
            sessionFinalKills++
        }
        if (event.killed == HypixelUtils.currentName) {
            sessionFinalDeaths++
        }
    }

    fun resetTracker() {
        secondsPassed = 0
        currentSeconds = 0
        averageSeconds = 0

        sessionGames = 0
        sessionWins = 0
        sessionLosses = 0

        sessionKills = 0
        sessionDeaths = 0

        sessionFinalKills = 0
        sessionFinalDeaths = 0

        sessionBedsBroken = 0
        sessionBedsLost = 0
    }

    private fun getRatio(good: Int, bad: Int): Double {
        val first = good.toDouble()
        val second = if (bad == 0) 1.0 else bad.toDouble()
        val result = first / second

        return result.round(2)
    }
}