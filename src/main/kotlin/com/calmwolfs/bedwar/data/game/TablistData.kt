package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.mixins.transformers.AccessorGuiPlayerTabOverlay
import com.calmwolfs.bedwar.utils.StringUtils.findMatcher
import com.calmwolfs.bedwar.utils.StringUtils.stripResets
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.world.WorldSettings
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object TablistData {
    var tablist = listOf<String>()
    var lobbyPlayers = listOf<String>()
    var header = ""
    var footer = ""

    private val tabUsernamePattern = "^(?:\\[\\w.+] )?(?:.\\s)?(?<username>\\w+)".toPattern()

    private val playerOrdering = Ordering.from(PlayerComparator())

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (!event.isMod(5)) return

        val thePlayer = Minecraft.getMinecraft()?.thePlayer ?: return
        if (thePlayer.sendQueue == null) return

        val players = playerOrdering.sortedCopy(thePlayer.sendQueue.playerInfoMap)
        val result = mutableListOf<String>()
        val playersResult = mutableListOf<String>()

        for (player in players) {
            val name = Minecraft.getMinecraft().ingameGUI.tabList.getPlayerName(player).stripResets()
            result.add(name)
            if (name.startsWith("§8[NPC]")) continue
            tabUsernamePattern.findMatcher(name.unformat()) {
                playersResult.add(group("username"))
            }
        }
        tablist = result
        lobbyPlayers = playersResult

        val tablistData = Minecraft.getMinecraft().ingameGUI.tabList as AccessorGuiPlayerTabOverlay
        header = tablistData.bedwar_getHeader().formattedText
        footer = tablistData.bedwar_getFooter().formattedText
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