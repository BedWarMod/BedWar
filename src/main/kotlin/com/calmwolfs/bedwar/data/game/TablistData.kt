package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.FooterUpdateEvent
import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.events.game.TablistScoresUpdateEvent
import com.calmwolfs.bedwar.events.game.TablistUpdateEvent
import com.calmwolfs.bedwar.mixins.transformers.AccessorGuiPlayerTabOverlay
import com.calmwolfs.bedwar.utils.StringUtils.findMatcher
import com.calmwolfs.bedwar.utils.StringUtils.stripResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.world.WorldSettings
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object TablistData {
    private var tablist = listOf<String>()
    private var lobbyPlayers = listOf<String>()
    private var header = ""
    private var footer = ""

    private var tablistScores = mapOf<String, Int>()
    private var tablistObjective = ""

    private val tabUsernamePattern = "^(?:\\[\\w.+] )?(?:.\\s)?(?<username>\\w+)".toPattern()

    private val playerOrdering = Ordering.from(PlayerComparator())

    fun getTablist() = tablist
    fun getPlayers() = lobbyPlayers
    fun getHeader() = header
    fun getFooter() = footer

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onTick(event: ModTickEvent) {
        val thePlayer = Minecraft.getMinecraft()?.thePlayer ?: return
        if (thePlayer.sendQueue == null) return

        val players = playerOrdering.sortedCopy(thePlayer.sendQueue.playerInfoMap)
        val result = mutableListOf<String>()

        for (player in players) {
            val name = Minecraft.getMinecraft().ingameGUI.tabList.getPlayerName(player).stripResets()
            result.add(name)
        }

        val tablistData = Minecraft.getMinecraft().ingameGUI.tabList as AccessorGuiPlayerTabOverlay
        header = tablistData.bedwar_getHeader().formattedText
        val footerData = tablistData.bedwar_getFooter().formattedText

        if (footerData != footer) {
            FooterUpdateEvent(footerData).postAndCatch()
            footer = footerData
        }

        val scoreboard = Minecraft.getMinecraft().theWorld?.scoreboard ?: return
        val objective = scoreboard.getObjectiveInDisplaySlot(0)
        if (objective != null) {
            tablistObjective = objective.displayName

            val scoreboardData = objective.scoreboard
            val playerScores = mutableMapOf<String, Int>()

            for (score in scoreboardData.scores) {
                val playerName = score.playerName

                if (playerName !in lobbyPlayers) continue

                val scoreValue = score.scorePoints
                playerScores[playerName] = scoreValue
            }

            if (playerScores != tablistScores) {
                tablistScores = playerScores
                TablistScoresUpdateEvent(tablistScores, tablistObjective).postAndCatch()
            }
        }

        if (result == tablist) return

        tablist = result

        val playersResult = mutableListOf<String>()
        for (line in tablist) {
            if (line.startsWith("§8[NPC]")) continue
            tabUsernamePattern.findMatcher(line.unformat()) {
                playersResult.add(group("username"))
            }
        }

        lobbyPlayers = playersResult

        TablistUpdateEvent(tablist).postAndCatch()
    }

    @SideOnly(Side.CLIENT)
    internal class PlayerComparator : Comparator<NetworkPlayerInfo> {
        override fun compare(o1: NetworkPlayerInfo, o2: NetworkPlayerInfo): Int {
            val team1 = o1.playerTeam
            val team2 = o2.playerTeam
            return ComparisonChain.start().compareTrueFirst(
                o1.gameType != WorldSettings.GameType.SPECTATOR,
                o2.gameType != WorldSettings.GameType.SPECTATOR
            ).compare(
                if (team1 != null) team1.registeredName else "",
                if (team2 != null) team2.registeredName else ""
            ).compare(o1.gameProfile.name, o2.gameProfile.name).result()
        }
    }
}