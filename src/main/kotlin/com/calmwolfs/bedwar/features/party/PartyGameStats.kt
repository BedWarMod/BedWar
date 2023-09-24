package com.calmwolfs.bedwar.features.party

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.types.BedwarsGameStat
import com.calmwolfs.bedwar.events.bedwars.*
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.PartyUtils
import com.calmwolfs.bedwar.utils.StringUtils
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
        BedWarMod.coroutineScope.launch {
            delay(1500.milliseconds)
            for ((player, stats) in gamePartyMembers) {
                if (player == HypixelUtils.currentName && !config.showSolo) continue

                val kills = StringUtils.optionalPlural(stats.kills, "§7Kill", "§7Kills")
                val finals = StringUtils.optionalPlural(stats.finals, "§7Final kill", "§7Final kills")
                val beds = StringUtils.optionalPlural(stats.beds, "§7Bed", "§7Beds")

                ModUtils.chat("§6[BedWar] §3$player §7got §6$kills, §6$finals and §6$beds")
            }
        }
    }
}