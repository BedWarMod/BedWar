package com.calmwolfs.bedwar.features.stats

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.eums.BedwarsGameMode
import com.calmwolfs.bedwar.data.types.BedwarsPlayerData
import com.calmwolfs.bedwar.events.PlayerJoinPartyEvent
import com.calmwolfs.bedwar.utils.ApiUtils
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.NumberUtils.addSeparators
import com.calmwolfs.bedwar.utils.NumberUtils.getRatio
import com.calmwolfs.bedwar.utils.NumberUtils.round
import com.calmwolfs.bedwar.utils.StatUtils
import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import kotlinx.coroutines.launch
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatStatDisplay {
    private val config get() =BedWarMod.feature.chat.playerStats

    @SubscribeEvent
    fun onPartyJoin(event: PlayerJoinPartyEvent) {
        if (!config.partyJoin) return
        displayStats(event.player, config.statType.get())
    }

    fun command(args: Array<String>) {
        if (args.isEmpty()) {
            displayStats(HypixelUtils.currentName, config.statType.get())
        }
        else {
            if (args.size == 1) {
                displayStats(args[0], config.statType.get())
            } else {
                val statType = BedwarsGameMode.entries.firstOrNull { it.shortName == args[1] } ?: config.statType.get()
                displayStats(args[0], statType)
            }
        }
    }

    private fun displayStats(player: String, type: BedwarsGameMode) {
        BedWarMod.coroutineScope.launch {
            val uuid = ApiUtils.getUuid(player)
            if (uuid == "--") {
                ChatUtils.chat("§eAlready fetching $player, slow down!")
                return@launch
            }

            if (uuid == "-") {
                ChatUtils.chat("§6[BW] §3$player §7is probably nicked!")
                return@launch
            }

            val playerStats = StatUtils.getPlayerData(uuid)

            if (playerStats.lastUpdated == SimpleTimeMark.farPast()) {
                ChatUtils.chat("§6[BW] §eProbably got rate limited for $player§7!")
                return@launch
            }

            displayChatStats(playerStats, type)
        }
    }

    private fun displayChatStats(stats: BedwarsPlayerData, type: BedwarsGameMode) {
        val player = stats.displayName
        if (player == "-") return
        val modeStats = when (type) {
            BedwarsGameMode.SOLOS -> stats.solosStats
            BedwarsGameMode.DOUBLES -> stats.doublesStats
            BedwarsGameMode.THREES -> stats.threesStats
            BedwarsGameMode.FOURS -> stats.foursStats
            BedwarsGameMode.FOURVFOUR -> stats.fourVsFourStats
            else -> stats.overallStats
        }

        val kdr = getRatio(modeStats.kills, modeStats.losses)
        val fkdr = getRatio(modeStats.finalKills, modeStats.finalDeaths)
        val bblr = getRatio(modeStats.bedsBroken, modeStats.bedsLost)
        val wlr = getRatio(modeStats.wins, modeStats.losses)
        val winstreak = if (modeStats.winstreak == -1) "§eAPI off" else modeStats.winstreak.addSeparators()
        val winRate = if (modeStats.wins + modeStats.losses > 0) {
            ((modeStats.wins.toDouble() / (modeStats.wins + modeStats.losses)) * 100).round(3)
        } else {
            "0.00%"
        }

        val buttonsLine = makeButtonsLine(type, player)

        ChatUtils.chat("")
        ChatUtils.chat("§6[BedWar] §7${type.displayName} stats for §3$player")
        ChatUtils.chat("§6[BW] §7Kills: §6${modeStats.kills.addSeparators()} §7| KDR: §6$kdr")
        ChatUtils.chat("§6[BW] §7Finals: §6${modeStats.finalKills.addSeparators()} §7| FKDR: §6$fkdr")
        ChatUtils.chat("§6[BW] §7Beds: §6${modeStats.bedsBroken.addSeparators()} §7| BBLR: §6$bblr")
        ChatUtils.chat("§6[BW] §7Wins: §6${modeStats.wins.addSeparators()} §7| WLR: §6$wlr")
        ChatUtils.chat("§6[BW] §7Win Rate: §6$winRate%")
        ChatUtils.chat("§6[BW] §7Winstreak: §6${winstreak}")
        ChatUtils.messagePlayer(buttonsLine)
        ChatUtils.chat("")
    }

    private fun makeButtonsLine(type: BedwarsGameMode, player: String): ChatComponentText {
        val baseMessage = ChatComponentText("")

        val clickableComponents = mutableListOf<ChatComponentText>()

        for (gameMode in BedwarsGameMode.entries) {
            if (gameMode != type) {
                clickableComponents.add(ChatUtils.makeClickableChat(
                    "§7[§6${gameMode.shortName}§7]",
                    "bws $player ${gameMode.shortName}",
                    "§7See the §a${gameMode.shortName} §7stats of §3$player"
                ))
            } else {
                val component = ChatComponentText("§7[§a${gameMode.shortName}§7]")
                component.chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("§aAlready Selected"))
                clickableComponents.add(component)
            }
        }

        for (component in clickableComponents) {
            baseMessage.appendSibling(component)
            baseMessage.appendSibling(ChatComponentText(" "))
        }

        return baseMessage
    }
}