package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.eums.BedwarsGameMode
import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.events.bedwars.StartGameEvent
import com.calmwolfs.bedwar.features.party.PartyCommands
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatCompleteUtils {
    private var gamePlayers = mutableListOf<String>()

    @SubscribeEvent
    fun onGameStart(event: StartGameEvent) {
        gamePlayers = TablistData.lobbyPlayers.toMutableList()
    }

    private fun getLobbyPlayers(): List<String> {
        if (BedwarsUtils.inBedwarsGame) {
            return gamePlayers
        }
        return TablistData.lobbyPlayers
    }

    @JvmStatic
    fun handleTabComplete(leftOfCursor: String, originalArray: Array<String>): Array<String>? {
        val splits = leftOfCursor.split(" ")
        if (splits.size > 1) {
            var command = splits.first().lowercase()
            if (command.startsWith("/")) {
                command = command.substring(1)
                customTabComplete(command)?.let {
                    return buildResponse(splits, (it + originalArray)).toSet().toTypedArray()
                }
            }
        }
        return null
    }

    private fun customTabComplete(command: String): List<String>? {
        if (command == "bws") {
            val resultList = getLobbyPlayers()
            return resultList + PartyUtils.partyMembers
        }

        if ((command == "pk" || command == "pt" || command == "pp") && BedWarMod.feature.party.shortCommands) {
            return PartyUtils.partyMembers
        }

        if (command == "p") {
            val resultList = getLobbyPlayers()
            return resultList + PartyCommands.otherPartyCommands
        }

        if (command == "play") {
            return BedwarsGameMode.getMapNames()
        }
        return null
    }

    private fun buildResponse(arguments: List<String>, fullResponse: List<String>): List<String> {
        if (arguments.size == 2) {
            val start = arguments[1].lowercase()
            return fullResponse.filter { it.lowercase().startsWith(start) }
        }
        return emptyList()
    }
}