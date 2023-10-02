package com.calmwolfs.bedwar.features.party

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.types.BedwarsGameStat
import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.utils.*
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.milliseconds

object PartyGameStats {
    // todo deal with nicks
    private val config get() = BedWarMod.feature.party.matchStats
    var gameMembers = mutableMapOf<String, BedwarsGameStat>()

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        gameMembers.clear()
        for (member in PartyUtils.partyMembers) {
            gameMembers[member] = BedwarsGameStat(0, 0, 0)
        }
        gameMembers[HypixelUtils.currentName] = BedwarsGameStat(0, 0, 0)
    }

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (event.killer in gameMembers) {
            val playerStats = gameMembers[event.killer] ?: BedwarsGameStat(0, 0, 0)
            playerStats.kills = playerStats.kills + 1
        }
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (event.killer in gameMembers) {
            val playerStats = gameMembers[event.killer] ?: BedwarsGameStat(0, 0, 0)
            playerStats.finals = playerStats.finals + 1
        }
    }

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (event.player in gameMembers) {
            val playerStats = gameMembers[event.player] ?: BedwarsGameStat(0, 0, 0)
            playerStats.beds = playerStats.beds + 1
        }
    }

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        if (!config.enabled) return
        val winner = event.winningTeam == BedwarsUtils.currentTeamName
        BedWarMod.coroutineScope.launch {
            delay(config.statsDelay.milliseconds)
            ChatUtils.chat("")
            ChatUtils.chat("§6[BedWar] §7Parties match stats")
            var line = ""
            for ((player, stats) in gameMembers) {
                val isPlayer = HypixelUtils.currentName == player

                val kills = StringUtils.optionalPlural(stats.kills, "§7Kill", "§7Kills")
                val finals = StringUtils.optionalPlural(stats.finals, "§7Final Kill", "§7Final Kills")
                val beds = StringUtils.optionalPlural(stats.beds, "§7Bed", "§7Beds")

                line = "§6[BW] §3$player §7got §6$kills§7, §6$finals §7and §6$beds"
                ChatUtils.chat(line)
                if (!winner && !config.sendOnLoss) continue
                if (isPlayer && config.actionType != 0) {
                    line = "${stats.kills} ${stats.finals} ${stats.beds}"
                    if (config.actionType == 1 || config.actionType == 3) {
                        ClipboardUtils.copyToClipboard(line)
                    }
                }
            }
            ChatUtils.chat("")
            if (!winner && !config.sendOnLoss) return@launch
            if (line != "" && (config.actionType == 2 || config.actionType == 3) && PartyUtils.partyMembers.isNotEmpty()) {
                delay(config.sendDelay.milliseconds)
                ChatUtils.sendCommandToServer("pc $line")
            }
        }
    }
}