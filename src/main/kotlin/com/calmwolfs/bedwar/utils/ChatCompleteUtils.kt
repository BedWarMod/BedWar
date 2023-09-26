package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.TablistData

object ChatCompleteUtils {
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
            val resultList = TablistData.lobbyPlayers
            return resultList + PartyUtils.partyMembers
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