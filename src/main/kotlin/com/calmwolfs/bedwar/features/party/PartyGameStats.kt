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

class PartyGameStats {
    // todo deal with nicks
    private val config get() = BedWarMod.feature.party.matchStats
    private var gamePartyMembers = mutableMapOf<String, BedwarsGameStat>()

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        gamePartyMembers.clear()
        for (member in PartyUtils.partyMembers) {
            gamePartyMembers[member] = BedwarsGameStat(0, 0, 0)
        }
        gamePartyMembers[HypixelUtils.currentName] = BedwarsGameStat(0, 0, 0)
    }

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (event.killer in gamePartyMembers) {
            val playerStats = gamePartyMembers[event.killer] ?: BedwarsGameStat(0, 0, 0)
            playerStats.kills = playerStats.kills + 1
        }
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (event.killer in gamePartyMembers) {
            val playerStats = gamePartyMembers[event.killer] ?: BedwarsGameStat(0, 0, 0)
            playerStats.finals = playerStats.finals + 1
        }
    }

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (event.player in gamePartyMembers) {
            val playerStats = gamePartyMembers[event.player] ?: BedwarsGameStat(0, 0, 0)
            playerStats.beds = playerStats.beds + 1
        }
    }

    @SubscribeEvent
    fun onGameEnd(event: EndGameEvent) {
        if (!config.enabled) return
        if (gamePartyMembers.size < 2 && !config.showSolo) return
        val winner = event.winningTeam == BedwarsUtils.currentTeam
        BedWarMod.coroutineScope.launch {
            delay(1250.milliseconds)
            ChatUtils.chat("")
            ChatUtils.chat("§6[BedWar] §7Parties match stats")
            var line = ""
            for ((player, stats) in gamePartyMembers) {
                val isPlayer = HypixelUtils.currentName == player
                if (isPlayer && !config.showSolo) continue

                val kills = StringUtils.optionalPlural(stats.kills, "§7Kill", "§7Kills")
                val finals = StringUtils.optionalPlural(stats.finals, "§7Final kill", "§7Final kills")
                val beds = StringUtils.optionalPlural(stats.beds, "§7Bed", "§7Beds")

                line = "§6[BW] §3$player §7got §6$kills§7, §6$finals §7and §6$beds"
                ChatUtils.chat(line)
                if (!winner && !config.sendOnLoss) continue
                if (isPlayer && config.actionType != 0) {
                    line = "[BW] ${stats.kills} ${stats.finals} ${stats.beds}"
                    if (config.actionType == 1 || config.actionType == 3) {
                        ClipboardUtils.copyToClipboard(line)
                        if (config.actionType == 1) {
                            ChatUtils.chat("§a[BedWar] §7Copied match stats to clipboard!")
                        }
                    }
                }
            }
            ChatUtils.chat("")
            if (!winner && !config.sendOnLoss) return@launch
            if (line != "" && (config.actionType == 2 || config.actionType == 3) && PartyUtils.partyMembers.isNotEmpty()) {
                delay(100.milliseconds)
                ChatUtils.sendCommandToServer("pc $line")
            }
        }
    }
}