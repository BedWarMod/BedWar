package com.calmwolfs.bedwar.features.team

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.data.types.BedwarsGameStat
import com.calmwolfs.bedwar.events.bedwars.FinalKillEvent
import com.calmwolfs.bedwar.events.bedwars.KillEvent
import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.events.game.TablistScoresUpdateEvent
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import com.calmwolfs.bedwar.features.party.PartyGameStats
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ListUtils.addAsSingletonList
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import com.calmwolfs.bedwar.utils.gui.GuiElementUtils.renderStringsAndItems
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

object TeamStatus {
    private val config get() = BedWarMod.feature.team.teamStatus
    private val tabTeamPattern = "^(?<team>\\w) (?:.\\s)?(?<username>\\w+)".toPattern()

    private val currentTeamMembers = mutableMapOf<String, PlayerStatus>()
    private var display = emptyList<List<Any>>()
    private var needsUpdate = false
    private val deadTeammates = mutableListOf<String>()
    private val eliminatedTeammates = mutableListOf<String>()
    private val disconnectedTeammates = mutableListOf<String>()

    fun getTeammates() {
        currentTeamMembers.clear()
        for (line in TablistData.getTablist()) {
            val firstChar = if (BedwarsUtils.currentTeamName == "Gray") 'S' else BedwarsUtils.currentTeamName[0]
            tabTeamPattern.matchMatcher(line.unformat()) {
                if (group("team")[0] == firstChar) {
                currentTeamMembers[group("username")] = PlayerStatus(20, SimpleTimeMark.farPast())
                    if (group("username") !in PartyGameStats.gameMembers) {
                        PartyGameStats.gameMembers[group("username")] = BedwarsGameStat(0, 0, 0)
                    }
                }
            }
        }
        needsUpdate = true
        deadTeammates.clear()
        eliminatedTeammates.clear()
        disconnectedTeammates.clear()
    }

    @SubscribeEvent
    fun onTablistScoresUpdate(event: TablistScoresUpdateEvent) {
        if (!BedwarsUtils.playingBedwars) return
        if (!config.enabled) return
        if (event.title != "§c❤") return
        for (score in event.scores) {
            if (score.key !in currentTeamMembers) continue
            val currentPlayerStatus = currentTeamMembers[score.key] ?: PlayerStatus(20, SimpleTimeMark.farPast())
            currentPlayerStatus.health = score.value
            currentTeamMembers[score.key] = currentPlayerStatus
            needsUpdate = true
        }
    }

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (event.killed !in currentTeamMembers) return
        val currentPlayerStatus = currentTeamMembers[event.killed] ?: PlayerStatus(20, SimpleTimeMark.farPast())
        currentPlayerStatus.respawnTime = SimpleTimeMark.now() + 5.seconds
        currentPlayerStatus.health = -1
        currentTeamMembers[event.killed] = currentPlayerStatus
        deadTeammates.add(event.killed)
        needsUpdate = true
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (event.killed !in currentTeamMembers) return
        val currentPlayerStatus = currentTeamMembers[event.killed] ?: PlayerStatus(20, SimpleTimeMark.farPast())
        currentPlayerStatus.health = -1
        currentTeamMembers[event.killed] = currentPlayerStatus
        eliminatedTeammates.add(event.killed)
        needsUpdate = true
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (!config.enabled) return
        if (!BedwarsUtils.inBedwarsGame) return
        if (!needsUpdate && deadTeammates.isEmpty()) return

        needsUpdate = false
        display = drawList()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!config.enabled) return
        if (!BedwarsUtils.inBedwarsGame) return
        if (currentTeamMembers.size == 1 && !config.showSolo) return

        config.position.renderStringsAndItems(display, posLabel = "Session Tracker")
    }

    private fun drawList() = buildList<List<Any>> {
        addAsSingletonList("§e§lTeam Status")
        for (teammate in currentTeamMembers) {
            if (teammate.value.respawnTime.isInPast()) {
                teammate.value.respawnTime = SimpleTimeMark.farPast()
                deadTeammates.remove(teammate.key)
            }

            val key = teammate.key
            val value = teammate.value
            val dead = key in deadTeammates
            val eliminated = key in eliminatedTeammates
            val disconnected = key in disconnectedTeammates
            val isFarPast = value.respawnTime == SimpleTimeMark.farPast()

            val health = when {
                value.health > 20 -> "§6${value.health}"
                value.health > 10 -> "§a${value.health}"
                value.health > 5 -> "§e${value.health}"
                else -> "§c${value.health}"
            }

            val status = when {
                disconnected -> "$key §7- §eDISCONNECTED"
                eliminated -> "$key §7- §cELIMINATED"
                dead -> "$key §7- §cDEAD §f${value.respawnTime.timeUntil()}"
                isFarPast && value.health != -1 -> "$key §7- $health§c❤"
                else -> "$key §7- §6???"
            }

            addAsSingletonList(status)
        }
    }

    fun playerDisconnect(player: String) {
        if (player !in currentTeamMembers) return
        disconnectedTeammates.add(player)
    }

    fun playerReconnect(player: String) {
        if (player !in currentTeamMembers) return
        disconnectedTeammates.remove(player)
        val currentPlayerStatus = currentTeamMembers[player] ?: PlayerStatus(20, SimpleTimeMark.farPast())
        currentPlayerStatus.respawnTime = SimpleTimeMark.now() + 10.seconds
        currentPlayerStatus.health = -1
        currentTeamMembers[player] = currentPlayerStatus
        if (player !in deadTeammates) deadTeammates.add(player)
        needsUpdate = true
    }

    class PlayerStatus(var health: Int, var respawnTime: SimpleTimeMark)
}