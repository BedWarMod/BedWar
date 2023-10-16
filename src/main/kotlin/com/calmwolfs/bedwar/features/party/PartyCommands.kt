package com.calmwolfs.bedwar.features.party

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.PartyUtils

object PartyCommands {
    private val config get() = BedWarMod.feature.party

    fun kickOffline() {
        if (!config.shortCommands) return
        if (PartyUtils.partyMembers.isEmpty()) return
        ChatUtils.sendCommandToServer("party kickoffline")
    }

    fun warp() {
        if (!config.shortCommands) return
        if (PartyUtils.partyMembers.isEmpty()) return
        ChatUtils.sendCommandToServer("party warp")
    }

    fun kick(args: Array<String>) {
        if (!config.shortCommands) return
        if (PartyUtils.partyMembers.isEmpty()) return
        if (args.isEmpty()) return
        ChatUtils.sendCommandToServer("party kick ${args[0]}")
    }

    fun transfer(args: Array<String>) {
        if (args.isEmpty()) ChatUtils.sendCommandToServer("pt")
        if (!config.shortCommands) return
        if (PartyUtils.partyMembers.isEmpty()) return
        ChatUtils.sendCommandToServer("party transfer ${args[0]}")
    }

    fun promote(args: Array<String>) {
        if (!config.shortCommands) return
        if (PartyUtils.partyMembers.isEmpty()) return
        if (args.isEmpty()) return
        ChatUtils.sendCommandToServer("party promote ${args[0]}")
    }

    val otherPartyCommands = listOf(
        "Disband",
        "KickOffline",
        "Leave",
        "List",
        "Mute",
        "Private",
        "Warp",
        "Settings"
    )
}