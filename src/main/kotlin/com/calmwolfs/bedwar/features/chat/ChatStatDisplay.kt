package com.calmwolfs.bedwar.features.chat

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.eums.BedwarsGameMode
import com.calmwolfs.bedwar.data.types.BedwarsPlayerData
import com.calmwolfs.bedwar.events.PlayerJoinPartyEvent
import com.calmwolfs.bedwar.utils.*
import com.calmwolfs.bedwar.utils.NumberUtils.addSeparators
import com.calmwolfs.bedwar.utils.NumberUtils.getRatio
import com.calmwolfs.bedwar.utils.NumberUtils.round
import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import kotlinx.coroutines.launch
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatStatDisplay {
    private val config get() = BedWarMod.feature.chat.playerStats

    @SubscribeEvent
    fun onPartyJoin(event: PlayerJoinPartyEvent) {
        if (!config.partyJoin) return
        displayStats(event.player, config.statType.get())
    }

    fun command(args: Array<String>) {
        if (BedWarMod.feature.dev.apiKey == "") {
            ModUtils.error("You need an api key to use this until a new system is made")
        }
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

        val stars = BedwarsStarUtils.getStarForExp(stats.experience)
        val kdr = getRatio(modeStats.kills, modeStats.losses)
        val fkdr = getRatio(modeStats.finalKills, modeStats.finalDeaths)
        val bblr = getRatio(modeStats.bedsBroken, modeStats.bedsLost)
        val wlr = getRatio(modeStats.wins, modeStats.losses)
        val winstreak = if (modeStats.winstreak == -1) "§eAPI off" else modeStats.winstreak.addSeparators()
        val winRate = if (modeStats.wins + modeStats.losses > 0) {
            ((modeStats.wins.toDouble() / (modeStats.wins + modeStats.losses)) * 100).round(3)
        } else {
            "0.00"
        }

        val outputLines = mutableListOf<ChatComponentText>()

        outputLines.add(ChatComponentText(""))
        //todo star colours and prestige name in thing
        outputLines.add(ChatUtils.makeHoverChat(
            "§6[BedWar] §7${type.displayName} stats for §3$player §7[§6${stars.toInt().addSeparators()}✫§7]",
            "§3$player §7has §6${stats.experience.addSeparators()} §7total bedwars experience!"
        ))
        outputLines.add(ChatUtils.makeMultiLineHoverChat(
            "§6[BW] §7Kills: §6${modeStats.kills.addSeparators()} §7| KDR: §6$kdr",
            listOf(
                "§7Kills: §6${modeStats.kills.addSeparators()}",
                "§7Deaths: §6${modeStats.deaths.addSeparators()}",
                "§7Kills per star: §6${(modeStats.kills / stars).round(2)}",
                "§7Deaths per star: §6${(modeStats.deaths / stars).round(2)}",
                "§7KDR: §6$kdr"
            )
        ))
        outputLines.add(ChatUtils.makeMultiLineHoverChat(
            "§6[BW] §7Finals: §6${modeStats.finalKills.addSeparators()} §7| FKDR: §6$fkdr",
            listOf(
                "§7Final Kills: §6${modeStats.finalKills.addSeparators()}",
                "§7Final Deaths: §6${modeStats.finalDeaths.addSeparators()}",
                "§7Final Kills per star: §6${(modeStats.finalKills / stars).round(2)}",
                "§7Final Deaths per star: §6${(modeStats.finalDeaths / stars).round(2)}",
                "§7FKDR: §6$fkdr"
            )
        ))
        outputLines.add(ChatUtils.makeMultiLineHoverChat(
            "§6[BW] §7Beds: §6${modeStats.bedsBroken.addSeparators()} §7| BBLR: §6$bblr",
            listOf(
                "§7Beds Broken: §6${modeStats.bedsBroken.addSeparators()}",
                "§7Beds Lost: §6${modeStats.bedsLost.addSeparators()}",
                "§7Beds Broken per star: §6${(modeStats.bedsBroken / stars).round(2)}",
                "§7Beds Lost per star: §6${(modeStats.bedsLost / stars).round(2)}",
                "§7BBLR: §6$bblr"
            )
        ))
        outputLines.add(ChatUtils.makeMultiLineHoverChat(
            "§6[BW] §7Wins: §6${modeStats.wins.addSeparators()} §7| WLR: §6$wlr",
            listOf(
                "§7Wins: §6${modeStats.wins.addSeparators()}",
                "§7Losses: §6${modeStats.losses.addSeparators()}",
                "§7Wins per star: §6${(modeStats.wins / stars).round(2)}",
                "§7Losses per star: §6${(modeStats.losses / stars).round(2)}",
                "§7WLR: §6$wlr"
            )
        ))
        outputLines.add(ChatComponentText("§6[BW] §7Win Rate: §6$winRate%"))
        outputLines.add(ChatComponentText("§6[BW] §7Winstreak: §6${winstreak}"))
        outputLines.add(makeButtonsLine(type, player))

        val outputLine = ChatComponentText("")
        for (line in outputLines) {
            outputLine.appendSibling(line)
            outputLine.appendSibling(ChatComponentText("\n"))
        }
        ChatUtils.messagePlayer(outputLine)
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